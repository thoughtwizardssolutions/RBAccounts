package com.accounts.rb.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounts.rb.domain.InvoiceType;
import com.accounts.rb.domain.UserSequence;
import com.accounts.rb.repository.UserSequenceRepository;

@Service
@Transactional
public class UserSequenceService {
  
  @Inject 
  private UserSequenceRepository userSequenceRepository;

  private final Logger log = LoggerFactory.getLogger(UserSequenceService.class);
  
  @Async
  public void updateUserSequence(String username, InvoiceType invoiceType) {
    log.info("Updating user sequence");
    List<UserSequence> userSequences = userSequenceRepository.findByCreatedBy(username);
    UserSequence userSequence = userSequences.get(0);
    if(invoiceType.equals(InvoiceType.TAX_INVOICE)) {
      userSequence.setTaxSequence(userSequence.getTaxSequence() + 1);
    } else if(invoiceType.equals(InvoiceType.SALES_INVOICE)) {
      userSequence.setSalesSequence(userSequence.getSalesSequence() + 1);
    } else {
      userSequence.setSampleInvoiceSequence(userSequence.getSampleInvoiceSequence() + 1);
    }
    userSequenceRepository.save(userSequence);
  }
}
