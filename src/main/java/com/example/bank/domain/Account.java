package com.example.bank.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object for accounts.
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class Account {

    @Builder.Default
    @Transient
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateUpdated = LocalDateTime.now();

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 8, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @ManyToOne(optional = false)
    private Customer customer;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();

}
