package com.accounts.rb.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.accounts.rb.domain.Imei;
import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;
import com.accounts.rb.domain.InvoiceReport;
import com.accounts.rb.domain.InvoiceSearchCommand;
import com.accounts.rb.domain.InvoiceType;
import com.accounts.rb.repository.InvoiceRepository;

/**
 * Service Interface for managing Invoice.
 */
@Service
@Transactional
public class InvoiceService {
  
  @Inject 
  InvoiceItemService invoiceItemService;
  
  @Inject
  private UserSequenceService userSequenceService;
  
  @Inject 
  InvoiceRepository invoiceRepository;

  @Inject 
  ProductTransactionService productTransactionService;


    /**
     * Save a invoiceItem.
     * 
     * @param invoiceItem the entity to save
     * @return the persisted entity
     */
    public Invoice save(Invoice invoice, User user) {
      for(InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
    	  if (!CollectionUtils.isEmpty(invoiceItem.getImeis())) {
	        for(Imei imei: invoiceItem.getImeis()) {
	          imei.setInvoiceItem(invoiceItem);
	        }
    	  }
        invoiceItem.setInvoice(invoice);
      }
      Invoice savedInvoice = invoiceRepository.save(invoice);
      updateUserSequence(savedInvoice, user);
      productTransactionService.saveInvoiceTransactions(invoice);
      return savedInvoice;
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

    /**
     *  find Invoices based on criteria only createdBy provided username
     *  
     *  @param search criteria, username
     */
    public List<InvoiceReport> findAllByCriteria(InvoiceSearchCommand criteria) {
      Instant fromInstant = Instant.ofEpochMilli(Long.valueOf(criteria.getFromDate()));
      ZonedDateTime fromTime =  ZonedDateTime.ofInstant(fromInstant, ZoneId.systemDefault());
      Instant toInstant = Instant.ofEpochMilli(Long.valueOf(criteria.getToDate()));
      ZonedDateTime toTime =  ZonedDateTime.ofInstant(toInstant, ZoneId.systemDefault());
      List<Object[]> result = new ArrayList<>();
      if(StringUtils.isBlank(criteria.getCreatedBy())) {
        result = invoiceRepository.findInvoicesByCriteria(fromTime, toTime);  
      } else {
        result = invoiceRepository.findInvoicesByCriteria(criteria.getCreatedBy(), fromTime, toTime);
      }
      List<InvoiceReport> list = new ArrayList<>();
      for(Object[] object : result) {
        String invoiceNumber = (String) object[0];
        String createdBy = (String) object[1];
        ZonedDateTime creationTime =  (ZonedDateTime) object[2];
        String customerName = (String) object[3];
        BigDecimal taxes = (BigDecimal) object[4];
        BigDecimal totalAmount = (BigDecimal) object[5];
        Long id = (Long) object[6];
        list.add(new InvoiceReport(invoiceNumber, creationTime, customerName, taxes, totalAmount, createdBy, id));
      }
       return list; 
    }
    
    /**
     * Updates Invoice sequence number for User after invoice is created
     * @param invoice
     * @param user
     */
    private void updateUserSequence(Invoice invoice, User user) {
      userSequenceService.updateUserSequence(user.getUsername(), InvoiceType.valueOf(invoice.getInvoiceType()));
    }

    public Invoice update(Invoice invoice, User currentUser) {
        for(InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                  if (!CollectionUtils.isEmpty(invoiceItem.getImeis())) {
                    for(Imei imei: invoiceItem.getImeis()) {
                      imei.setInvoiceItem(invoiceItem);
                    }
                  }
         invoiceItem.setInvoice(invoice);
        }
      Invoice savedInvoice = invoiceRepository.save(invoice);
      productTransactionService.updateInvoiceTransactions(invoice);
      return savedInvoice;
    }
}
