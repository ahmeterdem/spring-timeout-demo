package tr.org.tkk.esb.springtimeoutdemo.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.org.tkk.esb.springtimeoutdemo.repository.CustomerRepository;

/**
 * Prepared by ahmeterdem on 24.06.2025 for Project spring-timeout-demo.
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final Executor customTaskExecutor;

    public CustomerService(CustomerRepository customerRepository, Executor customTaskExecutor) {
        this.customerRepository = customerRepository;
        this.customTaskExecutor = customTaskExecutor;
    }

    @Transactional(timeout = 240)
    public void processCustomersWithTimeout() {

        CompletableFuture.runAsync(() -> {
                List<?> customers = customerRepository.findByName("John");
                customers.forEach(customer -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }, customTaskExecutor)
            .orTimeout(3, TimeUnit.MINUTES)
            .exceptionally(ex -> {
                System.out.println("Task timed out or failed: " + ex.getMessage());
                return null;
            });
    }
}