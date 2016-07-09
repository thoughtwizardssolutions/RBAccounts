package com.accounts.rb.service;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;
import com.accounts.rb.domain.Product;
import com.accounts.rb.domain.ProductTransaction;
import com.accounts.rb.repository.ProductRepository;
import com.accounts.rb.repository.ProductTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    
    @Inject
    private ProductRepository productRepository;
    
    /**
     * Asynchronously save productTransactions
     * To be used with invoice creation flow to capture product transactions related to an invoice
     * @param Invoice
     * @return 
     */
    @Async
    public void saveInvoiceTransactions(Invoice invoice) {
        log.debug("Request to save {} ProductTransactions", invoice.getInvoiceItems().size());
        List<ProductTransaction> productTransactions = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for(InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
          ProductTransaction productTransaction = new ProductTransaction();
          productTransaction.setCreationTime(ZonedDateTime.now());
          productTransaction.setInvoiceItem(invoiceItem);
          productTransaction.setInvoicedStock(Boolean.TRUE);
          productTransaction.setNewStock(Boolean.FALSE);
          productTransaction.setReturnStock(Boolean.FALSE);
          productTransaction.setQuantity(invoiceItem.getQuantity());
          productTransactions.add(productTransaction);
          productTransaction.setProduct(invoiceItem.getProduct());
          
          // update product availability
          productTransaction.getProduct().setModificationTime(ZonedDateTime.now());
          productTransaction.getProduct().setQuantity(productTransaction.getProduct().getQuantity() - productTransaction.getQuantity());
          productTransactions.add(productTransaction);
          products.add(productTransaction.getProduct());
          
        }
        productTransactionRepository.save(productTransactions);
        productRepository.save(products);
    }   
    
    /**
     * Asynchronously update productTransactions and products' available quantities
     * To be used with invoice creation flow to capture product transactions related to an invoice
     * @param List of InvoiceItems
     * @return the persisted entity
     */
    @Async
    public void updateInvoiceTransactions(Invoice invoice) {
        log.debug("Request to save {} ProductTransactions", invoice.getInvoiceItems().size());
        List<ProductTransaction> productTransactions = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for(InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
          ProductTransaction productTransaction = productTransactionRepository.findByInvoiceItem(invoiceItem).get(0);
          productTransaction.setModificationTime(ZonedDateTime.now());
          Integer oldQuantity = productTransaction.getQuantity();
          Product oldProduct = productTransaction.getProduct();
          // product is not updated
          if(oldProduct.getId().equals(invoiceItem.getProduct().getId())) {
            productTransaction.setQuantity(invoiceItem.getQuantity());
            // update product availability 
            Product product = productRepository.findOne(invoiceItem.getProduct().getId());
            product.setModificationTime(ZonedDateTime.now());
            Integer currentProductQuantity = product.getQuantity();
            Integer newQuantity = invoiceItem.getQuantity();
            if(newQuantity > oldQuantity) {
              product.setQuantity(currentProductQuantity - (newQuantity - oldQuantity));
            } else {
              product.setQuantity(currentProductQuantity + (oldQuantity - newQuantity));
            }
            productTransactions.add(productTransaction);
            products.add(product);
          } else {
            Product newProduct = productRepository.findOne(invoiceItem.getProduct().getId());
            newProduct.setModificationTime(ZonedDateTime.now());
            oldProduct.setModificationTime(ZonedDateTime.now());
            // update old product
            oldProduct.setQuantity(oldProduct.getQuantity() + oldQuantity);
            // update new product
            newProduct.setQuantity(newProduct.getQuantity() - invoiceItem.getQuantity());
            productTransactions.add(productTransaction);
            products.add(oldProduct);
            products.add(newProduct);
          }
        }
        productTransactionRepository.save(productTransactions);
        productRepository.save(products);
    }
    
    /**
     * Save a productTransaction.
     * 
     * @param productTransaction the entity to save
     * @return the persisted entity
     */
    public ProductTransaction save(ProductTransaction productTransaction) {
      log.debug("Request to save ProductTransaction : {}", productTransaction);
      // save transaction
      productTransaction.setCreationTime(ZonedDateTime.now());
      productTransaction.setInvoicedStock(false);
      ProductTransaction result = productTransactionRepository.save(productTransaction);
        
      // update product availability
      productTransaction.getProduct().setModificationTime(ZonedDateTime.now());
      productTransaction.getProduct().setQuantity(productTransaction.getProduct().getQuantity() + productTransaction.getQuantity());
      productRepository.save(productTransaction.getProduct());
        
      return result;
    }

    /**
     * Asynchronously save productTransaction when a new Product is created
     * To be used with product creation flow to capture product stock creation
     * @param Product
     * @return 
     */
    @Async
    public void saveProductTransactions(Product product) {
        log.debug("Request to save ProductTransaction for Product : {}", product);
          ProductTransaction productTransaction = new ProductTransaction();
          productTransaction.setCreationTime(ZonedDateTime.now());
          productTransaction.setInvoicedStock(Boolean.FALSE);
          productTransaction.setNewStock(Boolean.TRUE);
          productTransaction.setReturnStock(Boolean.FALSE);
          productTransaction.setQuantity(product.getQuantity());
          productTransaction.setProduct(product);
          productTransactionRepository.save(productTransaction);
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
