package com.accounts.rb.repository;

import com.accounts.rb.domain.ProductTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductTransactions entity.
 */
@SuppressWarnings("unused")
public interface ProductTransactionsRepository extends JpaRepository<ProductTransaction,Long> {

}
