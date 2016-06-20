package com.accounts.rb.repository;

import com.accounts.rb.domain.Imei;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import com.accounts.rb.domain.InvoiceItem;
import java.lang.Long;
import java.lang.String;

/**
 * Spring Data JPA repository for the Imei entity.
 */
@SuppressWarnings("unused")
public interface ImeiRepository extends JpaRepository<Imei,Long> {
  
  List<Imei> findByInvoiceItem(InvoiceItem invoiceitem);
  List<Imei> findById(Long id);
  List<Imei> findByImei1(String imei1);
  List<Imei> findByImei2(String imei2);

}
