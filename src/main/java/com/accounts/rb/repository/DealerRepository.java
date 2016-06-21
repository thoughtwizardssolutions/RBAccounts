package com.accounts.rb.repository;

import com.accounts.rb.domain.Dealer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.lang.String;

/**
 * Spring Data JPA repository for the Dealer entity.
 */
@SuppressWarnings("unused")
public interface DealerRepository extends JpaRepository<Dealer, Long> {
  Page<Dealer> findByCreatedBy(Pageable pageable, String createdby);

  Page<Dealer> findByFirmName(Pageable pageable, String firmname);

  Page<Dealer> findByOwnerName(Pageable pageable, String ownername);

  Page<Dealer> findByTin(Pageable pageable, String tin);

}
