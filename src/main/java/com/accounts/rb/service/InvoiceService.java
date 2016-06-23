package com.accounts.rb.service;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.accounts.rb.domain.Imei;
import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;
import com.accounts.rb.repository.InvoiceRepository;

/**
 * Service Interface for managing InvoiceItem.
 */
@Service
@Transactional
public class InvoiceService {
  
  @Inject 
  InvoiceItemService invoiceItemService;
  
  @Inject 
  InvoiceRepository invoiceRepository;

    /**
     * Save a invoiceItem.
     * 
     * @param invoiceItem the entity to save
     * @return the persisted entity
     */
    public Invoice save(Invoice invoice) {
      for(InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
    	  if (!CollectionUtils.isEmpty(invoiceItem.getImeis())) {
	        for(Imei imei: invoiceItem.getImeis()) {
	          imei.setInvoiceItem(invoiceItem);
	        }
    	  }
        invoiceItem.setInvoice(invoice);
      }
      return invoiceRepository.save(invoice);
    }

    /**
     *  Get all the invoiceItems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
  public Page<Invoice> findAll(Pageable pageable) {
    Page<Invoice> result = invoiceRepository.findAll(pageable);
    Iterator<Invoice> iter = result.iterator();
    while(iter.hasNext()) {
      Invoice invoice = iter.next();
      List<InvoiceItem> invoiceItems = invoiceItemService.findByInvoice(invoice.getId());
      invoice.setInvoiceItems(invoiceItems);
    }
    return result;
  }
  
  
  /**
   *  Get all the invoiceItems.
   *  
   *  @param pageable the pagination information
   *  @return the list of entities
   */
public Page<Invoice> findByCreatedBy(Pageable pageable, String createdBy) {
  Page<Invoice> result = invoiceRepository.findByCreatedBy(pageable, createdBy);
  Iterator<Invoice> iter = result.iterator();
  while(iter.hasNext()) {
    Invoice invoice = iter.next();
    List<InvoiceItem> invoiceItems = invoiceItemService.findByInvoice(invoice.getId());
    invoice.setInvoiceItems(invoiceItems);
  }
  return result;
}

    /**
     *  Get the "id" invoiceItem.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    public Invoice findOne(Long id) {
      Invoice invoice = invoiceRepository.findOne(id);
      List<InvoiceItem> invoiceItems = invoiceItemService.findByInvoice(invoice.getId());
      invoice.setInvoiceItems(invoiceItems);
      return invoice;
    }

    /**
     *  Delete the "id" invoiceItem.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
      invoiceRepository.delete(id);
    }
}
