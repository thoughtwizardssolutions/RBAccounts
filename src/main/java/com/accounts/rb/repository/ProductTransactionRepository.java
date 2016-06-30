package com.accounts.rb.repository;

import com.accounts.rb.domain.ProductTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductTransaction entity.
 */
@SuppressWarnings("unused")
public interface ProductTransactionRepository extends JpaRepository<ProductTransaction,Long> {

}
