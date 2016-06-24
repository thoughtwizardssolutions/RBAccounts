package com.accounts.rb.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class InvoiceReport {
  
  private String invoiceNumber;
  private ZonedDateTime invoiceDate;
  private String customerName;
  private BigDecimal taxAmount;
  private BigDecimal totalAmount;
  private String createdBy;
  private Long id;
  
  
  public InvoiceReport() {
    super();
  }

  public InvoiceReport(String invoiceNumber, ZonedDateTime invoiceDate, String customerName,
      BigDecimal taxAmount, BigDecimal totalAmount, String createdBy, Long id) {
    super();
    this.invoiceNumber = invoiceNumber;
    this.invoiceDate = invoiceDate;
    this.customerName = customerName;
    this.taxAmount = taxAmount;
    this.totalAmount = totalAmount;
    this.createdBy = createdBy;
    this.id = id;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public ZonedDateTime getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(ZonedDateTime invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public BigDecimal getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }


  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "InvoiceReport [invoiceNumber=" + invoiceNumber + ", invoiceDate=" + invoiceDate
        + ", customerName=" + customerName + ", taxAmount=" + taxAmount + ", totalAmount="
        + totalAmount + ", createdBy=" + createdBy + ", id=" + id + "]";
  }


        
}
