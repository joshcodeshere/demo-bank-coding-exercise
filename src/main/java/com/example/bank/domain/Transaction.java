package com.example.bank.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object for transactions.
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class Transaction {

    /**
     * SQL column length, public for DTO to have access for length validation.
     */
    public static final int DESCRIPTION_LENGTH = 32;

    @ManyToOne(optional = false)
    private Account account;

    @Column(nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @ManyToOne(optional = false)
    private Customer customer;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(length = DESCRIPTION_LENGTH, nullable = false)
    private String description;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Boolean isCurrent;

    @Column(length = 11, nullable = true)
    @Enumerated(EnumType.STRING)
    private TransactionTransferType transferType;

    @Column(length = 9, nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

}
