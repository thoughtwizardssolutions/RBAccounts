package com.accounts.rb.repository;

import com.accounts.rb.domain.ProductTransaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;

/**
 * Spring Data JPA repository for the ProductTransaction entity.
 */
@SuppressWarnings("unused")
public interface ProductTransactionRepository extends JpaRepository<ProductTransaction,Long> {

  List<ProductTransaction> findByInvoiceItem(InvoiceItem invoiceitem);

}
