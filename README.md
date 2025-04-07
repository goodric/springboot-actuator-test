# 此项目有AI生成

# Spring Boot Actuator Gateway Application

这是一个基于 Spring Boot 的应用程序，集成了 Spring Cloud Gateway 和 Actuator 功能，提供了完整的请求日志记录功能。

## 功能特点

- 基于 Spring Boot 3.2.3
- 使用 Spring Cloud Gateway 进行路由
- 集成 Spring Boot Actuator 监控端点
- 完整的请求日志记录系统
  - 记录所有 HTTP 请求（GET、POST 等）
  - 记录请求头信息
  - 记录 POST 请求的完整请求体
  - 记录请求处理时间和状态码
  - 同时支持控制台和文件日志输出

## 系统要求

- JDK 17 或更高版本
- Maven 3.6.0 或更高版本
- CentOS 或其他 Linux 发行版

## 快速开始

1. 克隆项目并进入项目目录：
```bash
cd spring-boot-test
```

2. 使用 Maven 编译项目：
```bash
mvn clean package
```

3. 运行应用：
```bash
./start.sh
```

## 配置说明

### 应用配置 (application.properties)

- 服务端口：8080
- Actuator 端点：全部启用
- 日志配置：
  - 日志级别：INFO
  - 日志文件：logs/application.log
  - 请求日志文件：logs/request.log

### 日志记录

应用会自动创建 `logs` 目录并记录以下信息：
- 应用启动和初始化信息
- 所有 HTTP 请求的详细信息
- POST 请求的完整请求体
- 请求处理时间和状态码

## API 测试

1. 测试 GET 请求：
```bash
curl http://localhost:8080/actuator/health
```

2. 测试 POST 请求：
```bash
curl -X POST -H "Content-Type: application/json" -d '{"key":"value"}' http://localhost:8080/actuator/test
```

## 监控端点

所有 Actuator 端点都在 `/actuator` 路径下可用，包括：
- `/actuator/health`：健康检查
- `/actuator/info`：应用信息
- `/actuator/metrics`：性能指标
- `/actuator/env`：环境变量
- 等等...

## 日志查看

1. 实时查看应用日志：
```bash
tail -f logs/application.log
```

2. 实时查看请求日志：
```bash
tail -f logs/request.log
```

## 注意事项

1. 确保 `logs` 目录有写入权限
2. 默认情况下，所有 Actuator 端点都是启用的
3. 生产环境部署时建议配置适当的安全措施

## 故障排除

如果遇到问题：
1. 检查日志文件中的错误信息
2. 确保端口 8080 未被占用
3. 验证 Maven 和 JDK 版本是否符合要求
4. 检查 `logs` 目录权限

## release v1.0.0为无鉴权

## release v1.1.0为有鉴权
添加了 Spring Security 依赖
创建了 SecurityConfig 配置类，实现了：基本认证（HTTP Basic Authentication），角色基础的访问控制，密码加密
![image](https://github.com/user-attachments/assets/79637df7-d1db-49b3-a893-37228f078e12)

![image](https://github.com/user-attachments/assets/48f4fe57-d26b-4064-9bbe-36cf639ec1a4)

如果你需要修改用户名和密码，可以在 application.properties 中更改 spring.security.user.name 和 spring.security.user.password 的值。
