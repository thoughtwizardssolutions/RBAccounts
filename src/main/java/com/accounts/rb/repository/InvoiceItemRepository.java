package com.accounts.rb.repository;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;
import com.accounts.rb.domain.Product;
import com.accounts.rb.service.ProductTransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;

/**
 * Spring Data JPA repository for the InvoiceItem entity.
 */
@SuppressWarnings("unused")
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem,Long> {

  final Logger log = LoggerFactory.getLogger(InvoiceItemRepository.class);
  
  @Query("SELECT p FROM InvoiceItem p WHERE p.invoice = LOWER(:invoice_id)")
  public List<InvoiceItem> findByInvoice(@Param("invoice_id") Long id);

}
