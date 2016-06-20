package com.accounts.rb.repository;

import com.accounts.rb.domain.Tax;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.lang.String;

/**
 * Spring Data JPA repository for the Tax entity.
 */
@SuppressWarnings("unused")
public interface TaxRepository extends JpaRepository<Tax,Long> {
  
  List<Tax> findByCreatedBy(String createdby);
  List<Tax> findByName(String name);

}
