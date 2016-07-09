package com.accounts.rb.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A InvoiceItem.
 */
@Entity
@Table(name = "invoice_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvoiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "mrp", precision=10, scale=2)
    private BigDecimal mrp;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;

    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "tax_rate", precision=10, scale=2)
    private BigDecimal taxRate;

    @Column(name = "discount", precision=10, scale=2)
    private BigDecimal discount;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "creation_time")
    private ZonedDateTime creationTime;

    @Column(name = "modification_time")
    private ZonedDateTime modificationTime;

    @Column(name = "color")
    private String color;

    @Column(name = "product_name")
    private String productName;
    
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @OneToMany(mappedBy = "invoiceItem", cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Imei> imeis = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="invoice_id")
    @JsonIgnore
    private Invoice invoice;
    
    @OneToOne(mappedBy = "invoiceItem", cascade = {CascadeType.ALL})
    @JsonIgnore
    private ProductTransaction productTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Product getProduct() {
      return product;
    }

    public void setProduct(Product product) {
      this.product = product;
    }

    public List<Imei> getImeis() {
        return imeis;
    }

    public void setImeis(List<Imei> imeis) {
        this.imeis = imeis;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ProductTransaction getProductTransaction() {
      return productTransaction;
    }

    public void setProductTransaction(ProductTransaction productTransaction) {
      this.productTransaction = productTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoiceItem invoiceItem = (InvoiceItem) o;
        if(invoiceItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, invoiceItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
            "id=" + id +
            ", mrp='" + mrp + "'" +
            ", description='" + description + "'" +
            ", amount='" + amount + "'" +
            ", taxType='" + taxType + "'" +
            ", taxRate='" + taxRate + "'" +
            ", discount='" + discount + "'" +
            ", quantity='" + quantity + "'" +
            ", creationTime='" + creationTime + "'" +
            ", modificationTime='" + modificationTime + "'" +
            ", color='" + color + "'" +
            ", productName='" + productName + "'" +
            '}';
    }
}
