package com.accounts.rb.repository;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
  
  @Query("SELECT p FROM Invoice p WHERE p.createdBy = LOWER(:created_by)")
  Page<Invoice> findByCreatedBy(Pageable pageable, @Param("created_by") String createdBy);

}
