package com.example.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bank.domain.Account;

/**
 * {@link JpaRepository} for {@link Account} objects.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * @param customerId
     * @return all account(s) for given customerId
     */
    List<Account> findByCustomerId(Long customerId);

}
