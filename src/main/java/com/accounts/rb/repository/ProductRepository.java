package com.accounts.rb.repository;

import com.accounts.rb.domain.Product;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.lang.String;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
public interface ProductRepository extends JpaRepository<Product,Long> {
  
List<Product> findByCreatedBy(String createdby);
List<Product> findByName(String name);
List<Product> findByColor(String color);

}
