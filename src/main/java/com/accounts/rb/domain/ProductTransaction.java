package com.accounts.rb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ProductTransaction.
 */
@Entity
@Table(name = "product_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    private ZonedDateTime creationTime;

    @Column(name = "modification_time")
    private ZonedDateTime modificationTime;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "new_stock")
    private Boolean newStock;

    @Column(name = "return_stock")
    private Boolean returnStock;

    @Column(name = "invoiced_stock")
    private Boolean invoicedStock;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
    
    @OneToOne
    @JoinColumn(name="invoice_item_id")
    private InvoiceItem invoiceItem;
    
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean isNewStock() {
        return newStock;
    }

    public void setNewStock(Boolean newStock) {
        this.newStock = newStock;
    }

    public Boolean isReturnStock() {
        return returnStock;
    }

    public void setReturnStock(Boolean returnStock) {
        this.returnStock = returnStock;
    }

    public Boolean isInvoicedStock() {
        return invoicedStock;
    }

    public void setInvoicedStock(Boolean invoicedStock) {
        this.invoicedStock = invoicedStock;
    }

    public Product getProduct() {
      return product;
    }

    public void setProduct(Product product) {
      this.product = product;
    }

    public InvoiceItem getInvoiceItem() {
      return invoiceItem;
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
      this.invoiceItem = invoiceItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductTransaction productTransaction = (ProductTransaction) o;
        if(productTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductTransaction{" +
            "id=" + id +
            ", creationTime='" + creationTime + "'" +
            ", modificationTime='" + modificationTime + "'" +
            ", quantity='" + quantity + "'" +
            ", newStock='" + newStock + "'" +
            ", returnStock='" + returnStock + "'" +
            ", invoicedStock='" + invoicedStock + "'" +
            '}';
    }
}
