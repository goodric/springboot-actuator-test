package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

@Configuration
@EnableWebFlux
public class LoggingConfig implements WebFluxConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FILE_PATH = "logs/request.log";
    
    static {
        // Ensure log directory exists
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        
        // Log startup message to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.println(LocalDateTime.now().format(formatter) + " - LoggingConfig initialized");
        } catch (Exception e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    @Bean
    public WebFilter loggingFilter() {
        return (exchange, chain) -> {
            long startTime = System.currentTimeMillis();
            
            // Log request
            String requestPath = exchange.getRequest().getPath().value();
            String method = exchange.getRequest().getMethod().name();
            String remoteAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            
            String logMessage = String.format("Incoming request - Time: %s, Method: %s, Path: %s, Remote IP: %s", 
                LocalDateTime.now().format(formatter), method, requestPath, remoteAddress);
            
            // Log to SLF4J
            logger.info(logMessage);
            
            // Also log directly to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                writer.println(logMessage);
            } catch (Exception e) {
                System.err.println("Failed to write to log file: " + e.getMessage());
            }

            // Log request headers
            exchange.getRequest().getHeaders().forEach((name, values) -> {
                logger.debug("Request header - {}: {}", name, values);
            });

            // For POST requests, log the request body
            if ("POST".equals(method)) {
                return exchange.getRequest().getBody()
                    .collectList()
                    .flatMap(dataBuffers -> {
                        if (dataBuffers.isEmpty()) {
                            // Log empty body
                            String emptyBodyMessage = String.format("Request body - Time: %s, Path: %s, Body: [Empty]", 
                                LocalDateTime.now().format(formatter), requestPath);
                            logger.info(emptyBodyMessage);
                            try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                                writer.println(emptyBodyMessage);
                            } catch (Exception e) {
                                System.err.println("Failed to write to log file: " + e.getMessage());
                            }
                            return chain.filter(exchange);
                        }

                        // Join all data buffers
                        DataBuffer joinedBuffers = exchange.getResponse().bufferFactory()
                            .join(dataBuffers);
                        
                        // Read the body content
                        byte[] bytes = new byte[joinedBuffers.readableByteCount()];
                        joinedBuffers.read(bytes);
                        
                        // Release the buffer
                        DataBufferUtils.release(joinedBuffers);
                        
                        // Convert to string
                        String body = new String(bytes, StandardCharsets.UTF_8);
                        
                        // Log the body
                        String bodyLogMessage = String.format("Request body - Time: %s, Path: %s, Body: %s", 
                            LocalDateTime.now().format(formatter), requestPath, body);
                        
                        // Log to SLF4J
                        logger.info(bodyLogMessage);
                        
                        // Also log directly to file
                        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                            writer.println(bodyLogMessage);
                        } catch (Exception e) {
                            System.err.println("Failed to write to log file: " + e.getMessage());
                        }
                        
                        return chain.filter(exchange);
                    });
            }

            return chain.filter(exchange)
                .doFinally(signalType -> {
                    long totalTime = System.currentTimeMillis() - startTime;
                    int status = exchange.getResponse().getStatusCode().value();
                    
                    String completionMessage = String.format("Request completed - Time: %s, Path: %s, Status: %d, Duration: %dms", 
                        LocalDateTime.now().format(formatter), requestPath, status, totalTime);
                    
                    // Log to SLF4J
                    logger.info(completionMessage);
                    
                    // Also log directly to file
                    try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                        writer.println(completionMessage);
                    } catch (Exception e) {
                        System.err.println("Failed to write to log file: " + e.getMessage());
                    }
                });
        };
    }
} 