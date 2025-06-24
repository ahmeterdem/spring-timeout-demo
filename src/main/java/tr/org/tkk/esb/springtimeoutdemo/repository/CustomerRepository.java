package tr.org.tkk.esb.springtimeoutdemo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import tr.org.tkk.esb.springtimeoutdemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Prepared by ahmeterdem on 24.06.2025 for Project spring-timeout-demo.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByName(String name);
}
