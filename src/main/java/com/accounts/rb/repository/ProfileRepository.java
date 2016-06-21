package com.accounts.rb.repository;

import com.accounts.rb.domain.Profile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Profile entity.
 */
public interface ProfileRepository extends JpaRepository<Profile,Long> {
	
	List<Profile> findByUser(String user);

}
