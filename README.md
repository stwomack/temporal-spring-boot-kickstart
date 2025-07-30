# Temporal Spring Boot Kickstart

A complete Spring Boot application demonstrating best practices for integrating [Temporal](https://temporal.io/) workflows with Spring Boot using the official `temporal-spring-boot-starter` auto-configuration.

This project showcases the **most "Spring Boot" way** to build Temporal applications, leveraging auto-discovery, auto-configuration, and Spring Boot conventions.

## Features

- **Full Spring Boot Auto-Configuration** - Zero manual bean configuration
- **Auto-Discovery** - Workflows and activities discovered automatically  
- **Annotation-Driven** - Using `@WorkflowImpl` and `@ActivityImpl`
- **Web GUI** - Built-in web interface for testing workflows
- **Exception Handling** - Centralized error handling with `@ControllerAdvice`
- **REST API** - Complete API for workflow execution and monitoring
- **Constructor Injection** - Following Spring Boot best practices
- **Ready for Testing** - Includes testing framework setup

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- [Temporal Server](https://docs.temporal.io/cli#install) (local or cloud)

### 1. Start Temporal Server

For local development, start Temporal with:

```bash
temporal server start-dev
```

This starts Temporal on `localhost:7233` with a web UI at `http://localhost:8233`.

### 2. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081`.

### 3. Test Workflows

#### Using the Web GUI
Open `http://localhost:8081` in your browser to access the interactive workflow testing interface.

#### Using the REST API
```bash
# Execute workflow synchronously
curl -X POST http://localhost:8081/api/workflow/execute \
  -H "Content-Type: application/json" \
  -d '{"input": "Hello Temporal!"}'

# Execute workflow asynchronously  
curl -X POST http://localhost:8081/api/workflow/execute-async \
  -H "Content-Type: application/json" \
  -d '{"input": "Hello Async!"}'

# Get workflow result by ID
curl http://localhost:8081/api/workflow/result/YOUR_WORKFLOW_ID
```

## Architecture

### Project Structure

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── activity/
│   │   │   ├── ExampleActivity.java          # Activity interface
│   │   │   └── ExampleActivityImpl.java      # Activity implementation
│   │   ├── controller/
│   │   │   ├── WebController.java            # Web GUI controller
│   │   │   └── WorkflowController.java       # REST API controller
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java   # Centralized error handling
│   │   │   └── WorkflowExecutionException.java
│   │   ├── service/
│   │   │   └── WorkflowClientService.java    # Workflow execution service
│   │   ├── workflow/
│   │   │   ├── ExampleWorkflow.java          # Workflow interface
│   │   │   └── ExampleWorkflowImpl.java      # Workflow implementation
│   │   └── TemporalSandboxApplication.java   # Spring Boot main class
│   └── resources/
│       ├── templates/
│       │   └── index.html                    # Web GUI template
│       └── application.yaml                  # Configuration
```

### Key Components

- **Workflows**: Business logic that needs to be durable and fault-tolerant
- **Activities**: Individual units of work that can fail and be retried
- **Web GUI**: Interactive interface for testing workflows
- **REST API**: Programmatic access to workflow operations
- **Exception Handling**: Proper error responses and logging

## Configuration

The application uses Spring Boot's auto-configuration with minimal setup:

```yaml
spring:
  temporal:
    connection:
      target: 127.0.0.1:7233      # Temporal server address
      namespace: default           # Temporal namespace
    workers-auto-discovery:
      packages:
        - com.example              # Package to scan for workflows/activities

server:
  port: 8081

logging:
  level:
    io.temporal: DEBUG
    com.example: DEBUG
```

### Temporal Cloud Configuration

For Temporal Cloud with mTLS:

```yaml
spring:
  temporal:
    connection:
      target: your-namespace.tmprl.cloud:7233
      namespace: your-namespace
      mtls:
        key-file: /path/to/your/key.key
        cert-chain-file: /path/to/your/cert.pem
```

For Temporal Cloud with API Key:

```yaml
spring:
  temporal:
    connection:
      target: your-namespace.tmprl.cloud:7233
      namespace: your-namespace
      api-key: your-api-key-here
```

### Testing Configuration

For integration tests with embedded Temporal:

```yaml
spring:
  temporal:
    test-server:
      enabled: true
```

## API Reference

### REST Endpoints

#### Execute Workflow (Synchronous)
- **POST** `/api/workflow/execute`
- **Body**: `{"input": "your input data"}`
- **Response**: Workflow result

#### Execute Workflow (Asynchronous)  
- **POST** `/api/workflow/execute-async`
- **Body**: `{"input": "your input data"}`
- **Response**: `"Workflow started with ID: workflow-id"`

#### Get Workflow Result
- **GET** `/api/workflow/result/{workflowId}`
- **Response**: Workflow result or error

### Web GUI

Access the web interface at `http://localhost:8081` for:
- Interactive workflow execution
- Async workflow management
- Result lookup by workflow ID
- Running workflows tracking

## Development

### Key Annotations

The project uses Temporal Spring Boot annotations for auto-discovery:

```java
@WorkflowImpl(taskQueues = "example-task-queue")
public class ExampleWorkflowImpl implements ExampleWorkflow {
    // Workflow implementation
}

@Component
@ActivityImpl(taskQueues = "example-task-queue")  
public class ExampleActivityImpl implements ExampleActivity {
    // Activity implementation
}
```

### Spring Boot Best Practices

- Constructor injection instead of `@Autowired` fields
- `@ControllerAdvice` for exception handling
- Configuration properties with `@ConfigurationProperties`
- Auto-configuration over manual bean creation
- Proper logging and error handling
- Input validation

### Adding New Workflows

1. Create workflow interface extending Temporal's workflow interface
2. Create implementation with `@WorkflowImpl(taskQueues = "your-queue")`
3. Update `application.yaml` if using a different task queue
4. Workflows are auto-discovered - no manual registration needed!

### Adding New Activities

1. Create activity interface with `@ActivityInterface`
2. Create implementation with `@Component` and `@ActivityImpl(taskQueues = "your-queue")`
3. Activities are auto-discovered and registered automatically

## Deployment

### Building

```bash
mvn clean package
```

### Running JAR

```bash
java -jar target/temporal-sandbox-0.0.1-SNAPSHOT.jar
```

### Docker

```dockerfile
FROM openjdk:17-jre-slim
COPY target/temporal-sandbox-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Learn More

- [Temporal Documentation](https://docs.temporal.io/)
- [Temporal Java SDK](https://docs.temporal.io/dev-guide/java)
- [Temporal Spring Boot Auto-Configure](https://github.com/temporalio/sdk-java/tree/master/temporal-spring-boot-autoconfigure)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Troubleshooting

### Common Issues

**Port already in use**: Change the port in `application.yaml`:
```yaml
server:
  port: 8082
```

**Cannot connect to Temporal**: Ensure Temporal server is running:
```bash
temporal server start-dev
```

**Workflow not found**: Check that workflow classes are in the auto-discovery package (`com.example`) and properly annotated.

### Support

- Check the [Temporal Community Forum](https://community.temporal.io/)
- Review [Temporal Slack](https://temporal.io/slack)
- Open an issue in this repository