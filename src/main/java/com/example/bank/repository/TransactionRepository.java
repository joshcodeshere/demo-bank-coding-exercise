package com.example.bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bank.domain.Transaction;

/**
 * {@link JpaRepository} for {@link Customer} objects.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * @param accountId
     * @return all transactions for given accountId
     */
    List<Transaction> findByAccountId(Long accountId);

    /**
     * @param accountId
     * @return the current transaction for the given accountId, which reflects the
     *         current balance
     */
    @Query("from Transaction t where t.account.id = :accountId and t.isCurrent = true")
    Optional<Transaction> findCurrentAccountIdTransaction(@Param("accountId") Long accountId);

}
