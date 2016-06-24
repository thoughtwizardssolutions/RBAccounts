package com.accounts.rb.domain;


public class InvoiceSearchCommand {
  
  private String createdBy;
  private String toDate;
  private String fromDate;
  
  public InvoiceSearchCommand() {
    super();
  }

  public InvoiceSearchCommand(String createdBy, String toDate, String fromDate) {
    super();
    this.createdBy = createdBy;
    this.toDate = toDate;
    this.fromDate = fromDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  @Override
  public String toString() {
    return "InvoiceSearchCommand [createdBy=" + createdBy + ", toDate=" + toDate + ", fromDate="
        + fromDate + "]";
  }
  
  
}
