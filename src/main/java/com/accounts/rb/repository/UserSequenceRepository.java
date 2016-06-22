package com.accounts.rb.repository;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.UserSequence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.lang.String;

/**
 * Spring Data JPA repository for the UserSequence entity.
 */
@SuppressWarnings("unused")
public interface UserSequenceRepository extends JpaRepository<UserSequence,Long> {
  
  List<UserSequence> findByCreatedBy(String createdby);
  
  @Query("UPDATE UserSequence p SET p.currentSequence  = p.currentSequence + 1 WHERE p.createdBy = LOWER(:created_by)")
  void incrementSequence(@Param("created_by") String createdBy);
  
}
