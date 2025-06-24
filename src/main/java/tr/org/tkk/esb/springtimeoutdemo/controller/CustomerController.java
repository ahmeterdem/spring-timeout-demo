package tr.org.tkk.esb.springtimeoutdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.org.tkk.esb.springtimeoutdemo.service.CustomerService;

/**
 * Prepared by ahmeterdem on 24.06.2025 for Project spring-timeout-demo.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> processCustomers() {
        try {
            customerService.processCustomersWithTimeout();
            return ResponseEntity.ok("Customer processing started.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
