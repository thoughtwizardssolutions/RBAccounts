package com.accounts.rb.repository;

import com.accounts.rb.domain.Dealer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dealer entity.
 */
@SuppressWarnings("unused")
public interface DealerRepository extends JpaRepository<Dealer,Long> {

}
