Global transaction timeout:	spring.transaction.default-timeout
Per-method transaction timeout:	@Transactional(timeout = 240)
JDBC query timeout:	javax.persistence.query.timeout
Per-task timeout:	CompletableFuture.orTimeout
ThreadPool tuning:	ThreadPoolTaskExecutor
Exception handling:	ControllerAdvice

Transaction Timeout: Configured via @Transactional(timeout = 240) and YAML (spring.transaction.default-timeout: 300).
Task Timeout: Managed using CompletableFuture.orTimeout().
ThreadPoolTaskExecutor: Custom pool with detailed settings.