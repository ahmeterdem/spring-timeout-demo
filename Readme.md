# Managing Transaction and Task Timeouts in Spring Boot: A Practical Guide

In complex enterprise applications, long-running database transactions and asynchronous tasks can lead to resource contention and unpredictable failures. In this article, I’ll walk you through a practical Spring Boot project that demonstrates how to properly configure **transaction timeouts**, manage **per-task timeouts**, and optimize **thread pools** using `ThreadPoolTaskExecutor`.


## 1. Project Overview

The sample project is a Spring Boot application named **Spring Timeout Demo**. It:

* Connects to an Oracle database using HikariCP
* Processes customer data with transaction timeouts
* Runs asynchronous tasks with per-task timeouts using `CompletableFuture`
* Handles timeout and general exceptions globally
* Runs easily with Maven or Docker



## 2. Transaction Timeout Management

Spring allows us to control **how long a database transaction can run** before it is forcefully rolled back. You can set a global transaction timeout in `application.yml`:

```yaml
spring:
  transaction:
    default-timeout: 300 # seconds
```

You can also set **method-specific timeouts** using the `@Transactional(timeout = ...)` annotation:

```java
@Transactional(timeout = 240)
public void processCustomersWithTimeout() {
    // Database operations
}
```


## 3. Async Task Timeout with CompletableFuture

Set timeout to asynchronous tasks using `CompletableFuture`:

```java
CompletableFuture.runAsync(() -> {
    // Simulate work
}, customTaskExecutor)
.orTimeout(3, TimeUnit.MINUTES)
.exceptionally(ex -> {
    System.out.println(\"Task timed out: \" + ex.getMessage());
    return null;
});
```

The `.orTimeout()` method ensures **per-task timeout enforcement**, which is useful for isolating slow-running tasks without killing the whole application.


## 4. Custom ThreadPool Configuration

Using `ThreadPoolTaskExecutor` provides better control over task concurrency:

```java
executor.setCorePoolSize(10);
executor.setMaxPoolSize(50);
executor.setQueueCapacity(2000);
executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
```

This setup:

* Provides room for scaling
* Avoids unbounded queues that could exhaust memory
* Uses a `CallerRunsPolicy` to ensure critical tasks are not dropped under load


## 5. Global Exception Handling

Timeout and general exceptions are handled centrally using Spring’s `@ControllerAdvice`:

```java
@ExceptionHandler(TimeoutException.class)
public ResponseEntity<String> handleTimeoutException(TimeoutException e) {
    return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(\"Task timed out.\");
}
```

## 6. Dockerizing the Application

The project includes a simple `Dockerfile`:

```Dockerfile
FROM openjdk:21-jdk-slim
COPY target/spring-timeout-demo-1.0.0.jar app.jar
ENTRYPOINT [\"java\",\"-jar\",\"/app.jar\"]
```



## 7. Summary
This project demonstrates a **complete strategy** to:
* Control database transaction durations
* Apply per-task timeout protections
* Manage thread pool resources effectively
* Handle timeouts gracefully


## GitHub Repository
https://github.com/ahmeterdem/spring-timeout-demo

Global transaction timeout:	spring.transaction.default-timeout
Per-method transaction timeout:	@Transactional(timeout = 240)
JDBC query timeout:	javax.persistence.query.timeout
Per-task timeout:	CompletableFuture.orTimeout
ThreadPool tuning:	ThreadPoolTaskExecutor
Exception handling:	ControllerAdvice

Transaction Timeout: Configured via @Transactional(timeout = 240) and YAML (spring.transaction.default-timeout: 300).
Task Timeout: Managed using CompletableFuture.orTimeout().
ThreadPoolTaskExecutor: Custom pool with detailed settings.