package com.accounts.rb.service;

import com.accounts.rb.domain.ProductTransaction;
import com.accounts.rb.repository.ProductTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ProductTransaction.
 */
@Service
@Transactional
public class ProductTransactionService {

    private final Logger log = LoggerFactory.getLogger(ProductTransactionService.class);
    
    @Inject
    private ProductTransactionRepository productTransactionRepository;
    
    /**
     * Save a productTransaction.
     * 
     * @param productTransaction the entity to save
     * @return the persisted entity
     */
    public ProductTransaction save(ProductTransaction productTransaction) {
        log.debug("Request to save ProductTransaction : {}", productTransaction);
        ProductTransaction result = productTransactionRepository.save(productTransaction);
        return result;
    }

    /**
     *  Get all the productTransactions.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProductTransaction> findAll() {
        log.debug("Request to get all ProductTransactions");
        List<ProductTransaction> result = productTransactionRepository.findAll();
        return result;
    }

    /**
     *  Get one productTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProductTransaction findOne(Long id) {
        log.debug("Request to get ProductTransaction : {}", id);
        ProductTransaction productTransaction = productTransactionRepository.findOne(id);
        return productTransaction;
    }

    /**
     *  Delete the  productTransaction by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductTransaction : {}", id);
        productTransactionRepository.delete(id);
    }
}
