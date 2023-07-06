package com.example.bank.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object for customers.
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class Customer {

    /**
     * SQL column length, public for DTO to have access for length validation.
     */
    public static final int NAME_LENGTH = 32;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<Account> accounts = new HashSet<>();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dateUpdated = LocalDateTime.now();

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = NAME_LENGTH, nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Long ssn;

}
