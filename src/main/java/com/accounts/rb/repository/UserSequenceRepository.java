package com.accounts.rb.repository;

import com.accounts.rb.domain.UserSequence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserSequence entity.
 */
public interface UserSequenceRepository extends JpaRepository<UserSequence,Long> {
  
  List<UserSequence> findByCreatedBy(String createdby);
  
}
