package com.accounts.rb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.accounts.rb.domain.ProductTransaction;
import com.accounts.rb.repository.ProductRepository;
import com.accounts.rb.service.ProductTransactionService;
import com.accounts.rb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProductTransaction.
 */
@RestController
@RequestMapping("/api")
public class ProductTransactionResource {

    private final Logger log = LoggerFactory.getLogger(ProductTransactionResource.class);
        
    @Inject
    private ProductTransactionService productTransactionService;
    
    @Inject
    private ProductRepository productRepository;
    /**
     * POST  /product-transactions : Create a new productTransaction.
     *
     * @param productTransaction the productTransaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productTransaction, or with status 400 (Bad Request) if the productTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-transactions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductTransaction> createProductTransaction(@Valid @RequestBody ProductTransaction productTransaction) throws URISyntaxException {
        log.debug("REST request to save ProductTransaction : {}", productTransaction);
        if (productTransaction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productTransaction", "idexists", "A new productTransaction cannot already have an ID")).body(null);
        }
        ProductTransaction result = productTransactionService.save(productTransaction);
        return ResponseEntity.created(new URI("/api/product-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-transactions : Updates an existing productTransaction.
     *
     * @param productTransaction the productTransaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productTransaction,
     * or with status 400 (Bad Request) if the productTransaction is not valid,
     * or with status 500 (Internal Server Error) if the productTransaction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-transactions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductTransaction> updateProductTransaction(@Valid @RequestBody ProductTransaction productTransaction) throws URISyntaxException {
        log.debug("REST request to update ProductTransaction : {}", productTransaction);
        if (productTransaction.getId() == null) {
            return createProductTransaction(productTransaction);
        }
        ProductTransaction result = productTransactionService.save(productTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productTransaction", productTransaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-transactions : get all the productTransactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productTransactions in body
     */
    @RequestMapping(value = "/product-transactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductTransaction> getAllProductTransactions() {
        log.debug("REST request to get all ProductTransactions");
        return productTransactionService.findAll();
    }

    /**
     * GET  /product-transactions/:id : get the "id" productTransaction.
     *
     * @param id the id of the productTransaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productTransaction, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/product-transactions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductTransaction> getProductTransaction(@PathVariable Long id) {
        log.debug("REST request to get ProductTransaction : {}", id);
        ProductTransaction productTransaction = productTransactionService.findOne(id);
        return Optional.ofNullable(productTransaction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /product-transactions/:id : delete the "id" productTransaction.
     *
     * @param id the id of the productTransaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/product-transactions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductTransaction(@PathVariable Long id) {
        log.debug("REST request to delete ProductTransaction : {}", id);
        productTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productTransaction", id.toString())).build();
    }

    public ProductRepository getProductRepository() {
      return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
      this.productRepository = productRepository;
    }

}
