package com.example.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateAccountDto;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.CustomerNotFoundException;
import com.example.bank.repository.AccountRepository;

/**
 * Business logic for accounts
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    @Lazy
    private TransactionService transactionService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Account create(CreateAccountDto createAccountDto) {
        Customer customer = customerService.findById(createAccountDto.getCustomerId());
        return repository.save(Account.builder().customer(customer)
                .type(createAccountDto.getType()).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(Long accountId) {
        return repository.existsById(accountId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Account> findAccountsByCustomerId(Long customerId) {
        if (!customerService.exists(customerId)) {
            throw new CustomerNotFoundException();
        }
        List<Account> accounts = repository.findByCustomerId(customerId);
        if (accounts == null || accounts.isEmpty()) {
            throw new AccountNotFoundException();
        }
        for (Account account : accounts) {
            account.setBalance(transactionService.getCurrentAccountBalance(account.getId()));
        }
        return accounts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account findById(Long accountId) {
        Optional<Account> account = repository.findById(accountId);
        if (!account.isPresent()) {
            throw new AccountNotFoundException();
        }
        account.get().setBalance(transactionService.getCurrentAccountBalance(account.get().getId()));
        return account.get();
    }

}
