package com.accounts.rb.repository;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice,Long>, JpaSpecificationExecutor<Invoice>  {
  
  @Query("SELECT p FROM Invoice p WHERE p.createdBy = LOWER(:created_by)")
  Page<Invoice> findByCreatedBy(Pageable pageable, @Param("created_by") String createdBy);

  
  @Query("SELECT p.invoiceNumber,p.createdBy, p.creationTime,q.firmName,p.taxes,p.totalAmount, p.id FROM Invoice p, Dealer q WHERE p.createdBy = LOWER(:created_by) AND q.id = p.dealerId AND p.creationTime >= :from_time AND p.creationTime <= :to_time")
  List<Object[]> findInvoicesByCriteria( @Param("created_by") String createdBy, @Param("from_time") ZonedDateTime fromTime,@Param("to_time") ZonedDateTime toTime);
  
  @Query("SELECT p.invoiceNumber,p.createdBy, p.creationTime,q.firmName,p.taxes,p.totalAmount, p.id FROM Invoice p, Dealer q WHERE q.id = p.dealerId AND p.creationTime >= :from_time AND p.creationTime <= :to_time")
  List<Object[]> findInvoicesByCriteria(@Param("from_time") ZonedDateTime fromTime,@Param("to_time") ZonedDateTime toTime);
}
