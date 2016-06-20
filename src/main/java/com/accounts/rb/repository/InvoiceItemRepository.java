package com.accounts.rb.repository;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the InvoiceItem entity.
 */
@SuppressWarnings("unused")
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem,Long> {

  @Query("SELECT p FROM InvoiceItem p WHERE p.invoice = LOWER(:invoice_id)")
  public List<InvoiceItem> findByInvoice(@Param("invoice_id") Long id);

}
