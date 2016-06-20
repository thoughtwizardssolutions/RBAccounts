package com.accounts.rb.service;

import com.accounts.rb.domain.Imei;
import com.accounts.rb.domain.InvoiceItem;
import com.accounts.rb.repository.ImeiRepository;
import com.accounts.rb.repository.InvoiceItemRepository;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing InvoiceItem.
 */
@Service
@Transactional
public class InvoiceItemService {
  
  @Inject 
  InvoiceItemRepository invoiceItemRepository;
  
  @Inject 
  ImeiRepository imeiRepository;

    /**
     * Save a invoiceItem.
     * 
     * @param invoiceItem the entity to save
     * @return the persisted entity
     */
    public InvoiceItem save(InvoiceItem invoiceItem) {
      for(Imei imei : invoiceItem.getImeis()) {
        imei.setInvoiceItem(invoiceItem);
      }
      return invoiceItemRepository.save(invoiceItem);
    }

    /**
     *  Get all the invoiceItems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
  public Page<InvoiceItem> findAll(Pageable pageable) {
    Page<InvoiceItem> result = invoiceItemRepository.findAll(pageable);
    for (InvoiceItem invoiceItem : result) {
      List<Imei> imeis = imeiRepository.findByInvoiceItem(invoiceItem);
      invoiceItem.setImeis(imeis);
    }
    return result;
  }
  
  
  /**
   *  Get all the invoiceItems.
   *  
   *  @param pageable the pagination information
   *  @return the list of entities
   */
public List<InvoiceItem> findByInvoice(Long id) {
  List<InvoiceItem> result = invoiceItemRepository.findByInvoice(id);
  for (InvoiceItem invoiceItem : result) {
    List<Imei> imeis = imeiRepository.findByInvoiceItem(invoiceItem);
    invoiceItem.setImeis(imeis);
  }
  return result;
}
  

    /**
     *  Get the "id" invoiceItem.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    public InvoiceItem findOne(Long id) {
      return invoiceItemRepository.findOne(id);
    }

    /**
     *  Delete the "id" invoiceItem.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id) {
      invoiceItemRepository.delete(id);
    }
}
