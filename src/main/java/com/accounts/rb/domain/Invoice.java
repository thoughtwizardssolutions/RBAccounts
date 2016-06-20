package com.accounts.rb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time")
    private ZonedDateTime creationTime;

    @Column(name = "modification_time")
    private ZonedDateTime modificationTime;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "sales_person_name")
    private String salesPersonName;

    @Column(name = "shipping_charges", precision=10, scale=2)
    private BigDecimal shippingCharges;

    @NotNull
    @Column(name = "subtotal", precision=10, scale=2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "taxes", precision=10, scale=2)
    private BigDecimal taxes;

    @Column(name = "adjustments", precision=10, scale=2)
    private BigDecimal adjustments;

    @NotNull
    @Column(name = "total_amount", precision=10, scale=2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "created_by")
    private String createdBy;

    @NotNull
    @Column(name = "dealer_id", nullable = false)
    private Long dealerId;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<InvoiceItem> invoiceItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public ZonedDateTime getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(ZonedDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public BigDecimal getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(BigDecimal shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(BigDecimal adjustments) {
        this.adjustments = adjustments;
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

    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        if(invoice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, invoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + id +
            ", creationTime='" + creationTime + "'" +
            ", modificationTime='" + modificationTime + "'" +
            ", invoiceNumber='" + invoiceNumber + "'" +
            ", orderNumber='" + orderNumber + "'" +
            ", salesPersonName='" + salesPersonName + "'" +
            ", shippingCharges='" + shippingCharges + "'" +
            ", subtotal='" + subtotal + "'" +
            ", taxes='" + taxes + "'" +
            ", adjustments='" + adjustments + "'" +
            ", totalAmount='" + totalAmount + "'" +
            ", createdBy='" + createdBy + "'" +
            ", dealerId='" + dealerId + "'" +
            '}';
    }
}
