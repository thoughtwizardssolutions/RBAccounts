package com.accounts.rb.repository;

import com.accounts.rb.domain.UserSequence;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserSequence entity.
 */
public interface UserSequenceRepository extends JpaRepository<UserSequence,Long> {
  
  List<UserSequence> findByCreatedBy(String createdby);
  
  @Query("UPDATE UserSequence p SET p.taxSequence  = p.taxSequence + 1 WHERE p.createdBy = LOWER(:created_by)")
  void incrementTaxSequence(@Param("created_by") String createdBy);
  
  @Query("UPDATE UserSequence p SET p.salesSequence  = p.salesSequence + 1 WHERE p.createdBy = LOWER(:created_by)")
  void incrementSalesSequence(@Param("created_by") String createdBy);
  
  @Query("UPDATE UserSequence p SET p.sampleSequence  = p.sampleSequence + 1 WHERE p.createdBy = LOWER(:created_by)")
  void incrementSampleSequence(@Param("created_by") String createdBy);
  
  
}
