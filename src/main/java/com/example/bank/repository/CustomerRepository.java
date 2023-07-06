package com.example.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bank.domain.Customer;

/**
 * {@link JpaRepository} for {@link Customer} objects.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * @param ssn
     * @return true if a {@link Customer} with given ssn already exists
     */
    boolean existsCustomerBySsn(Long ssn);

}
