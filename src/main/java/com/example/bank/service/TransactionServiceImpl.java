package com.example.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Transaction;
import com.example.bank.domain.TransactionTransferType;
import com.example.bank.domain.TransactionType;
import com.example.bank.dto.CreateDepositDto;
import com.example.bank.dto.CreateTransferDto;
import com.example.bank.dto.CreateWithdrawlDto;
import com.example.bank.exception.AccountDoesNotBelongToCustomerException;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.DepositMustBeGreaterThanZeroException;
import com.example.bank.exception.TransactionHasInsufficientFundsException;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.exception.TransactionSourceCannotEqualDestination;
import com.example.bank.exception.TransferMustBeGreaterThanZeroException;
import com.example.bank.repository.TransactionRepository;

/**
 * Business logic for customers.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction findById(Long transactionId) {
        Optional<Transaction> transaction = repository.findById(transactionId);
        if (!transaction.isPresent()) {
            throw new TransactionNotFoundException();
        }
        return transaction.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getCurrentAccountBalance(Long accountId) {
        Optional<Transaction> transaction = repository.findCurrentAccountIdTransaction(accountId);
        if (!transaction.isPresent()) {
            return BigDecimal.ZERO;
        }
        return transaction.get().getCurrentBalance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        if (!accountService.exists(accountId)) {
            throw new AccountNotFoundException();
        }
        List<Transaction> transactions = repository.findByAccountId(accountId);
        if (transactions == null || transactions.isEmpty()) {
            throw new TransactionNotFoundException();
        }
        return transactions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction process(CreateDepositDto createDepositDto) {
        if (createDepositDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DepositMustBeGreaterThanZeroException();
        }

        Account account = accountService.findById(createDepositDto.getAccountId());

        if (!createDepositDto.getCustomerId().equals(account.getCustomer().getId())) {
            throw new AccountDoesNotBelongToCustomerException();
        }

        Transaction deposit = new Transaction();
        deposit.setAccount(account);
        deposit.setAmount(createDepositDto.getAmount().abs());

        Optional<Transaction> currentTransaction = repository.findCurrentAccountIdTransaction(account.getId());
        if (currentTransaction.isPresent()) {
            deposit.setCurrentBalance(currentTransaction.get().getCurrentBalance().add(createDepositDto.getAmount()));

            currentTransaction.get().setIsCurrent(false);
            repository.save(currentTransaction.get());
        } else {
            deposit.setCurrentBalance(deposit.getAmount());
        }

        deposit.setCustomer(account.getCustomer());
        deposit.setDescription(createDepositDto.getDescription());
        deposit.setIsCurrent(true);
        deposit.setType(TransactionType.DEPOSIT);

        return repository.save(deposit);
    }

    @Override
    public List<Transaction> process(CreateTransferDto createTransferDto) {
        if (createTransferDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferMustBeGreaterThanZeroException();
        }

        Account sourceAccount = accountService.findById(createTransferDto.getAccountId());

        if (!createTransferDto.getCustomerId().equals(sourceAccount.getCustomer().getId())) {
            throw new AccountDoesNotBelongToCustomerException();
        }

        Account destinationAccount = accountService.findById(createTransferDto.getDestinationAccountId());

        if (destinationAccount.getId().equals(sourceAccount.getId())) {
            throw new TransactionSourceCannotEqualDestination();
        }

        // Check the source account for sufficient transfer funds
        BigDecimal sourceAccountBalanceAfterTransfer;
        Optional<Transaction> currentSourceTransaction = repository
                .findCurrentAccountIdTransaction(sourceAccount.getId());
        if (currentSourceTransaction.isPresent()) {

            sourceAccountBalanceAfterTransfer = currentSourceTransaction.get().getCurrentBalance()
                    .subtract(createTransferDto.getAmount().abs());

            if (sourceAccountBalanceAfterTransfer.compareTo(BigDecimal.ZERO) <= 0) {
                throw new TransactionHasInsufficientFundsException();
            }

        } else {
            throw new TransactionHasInsufficientFundsException();
        }

        // Build the source account transaction
        Transaction sourceTransaction = new Transaction();
        sourceTransaction.setAccount(sourceAccount);
        sourceTransaction.setAmount(createTransferDto.getAmount().abs().multiply(new BigDecimal(-1)));
        sourceTransaction.setCurrentBalance(sourceAccountBalanceAfterTransfer);
        sourceTransaction.setCustomer(sourceAccount.getCustomer());
        sourceTransaction.setDescription(createTransferDto.getDescription());
        sourceTransaction.setIsCurrent(true);
        sourceTransaction.setTransferType(TransactionTransferType.SOURCE);
        sourceTransaction.setType(TransactionType.TRANSFER);

        // Build the destination account transaction
        Transaction destinationTransaction = new Transaction();
        destinationTransaction.setAccount(destinationAccount);
        destinationTransaction.setAmount(createTransferDto.getAmount().abs());
        destinationTransaction
                .setCurrentBalance(destinationTransaction.getCurrentBalance().add(createTransferDto.getAmount().abs()));
        destinationTransaction.setCustomer(destinationAccount.getCustomer());
        destinationTransaction.setDescription(createTransferDto.getDescription());
        destinationTransaction.setIsCurrent(true);
        sourceTransaction.setTransferType(TransactionTransferType.DESTINATION);
        destinationTransaction.setType(TransactionType.TRANSFER);

        List<Transaction> transactions = new ArrayList<>(2);
        transactions.add(destinationTransaction);
        transactions.add(sourceTransaction);

        return repository.saveAll(transactions);
    }

    @Override
    public Transaction process(CreateWithdrawlDto createWithdrawlDto) {
        if (createWithdrawlDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferMustBeGreaterThanZeroException();
        }

        Account account = accountService.findById(createWithdrawlDto.getAccountId());

        if (!createWithdrawlDto.getCustomerId().equals(account.getCustomer().getId())) {
            throw new AccountDoesNotBelongToCustomerException();
        }

        // Check the account for sufficient withdrawl funds
        BigDecimal accountBalanceAfterWithdrawl;
        Optional<Transaction> currentTransaction = repository
                .findCurrentAccountIdTransaction(account.getId());
        if (currentTransaction.isPresent()) {

            accountBalanceAfterWithdrawl = currentTransaction.get().getCurrentBalance()
                    .subtract(createWithdrawlDto.getAmount().abs());

            if (accountBalanceAfterWithdrawl.compareTo(BigDecimal.ZERO) <= 0) {
                throw new TransactionHasInsufficientFundsException();
            }

        } else {
            throw new TransactionHasInsufficientFundsException();
        }

        // Build the account withdrawl transaction
        Transaction withdrawl = new Transaction();
        withdrawl.setAccount(account);
        withdrawl.setAmount(createWithdrawlDto.getAmount().abs().multiply(new BigDecimal(-1)));
        withdrawl.setCurrentBalance(accountBalanceAfterWithdrawl);
        withdrawl.setCustomer(account.getCustomer());
        withdrawl.setDescription(createWithdrawlDto.getDescription());
        withdrawl.setIsCurrent(true);
        withdrawl.setType(TransactionType.TRANSFER);

        return repository.save(withdrawl);

    }

}
