package com.accounts.rb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "mrp", precision=10, scale=2, nullable = false)
    private BigDecimal mrp;

    @Column(name = "color")
    private String color;

    @Column(name = "creation_time")
    private ZonedDateTime creationTime;

    @Column(name = "modification_time")
    private ZonedDateTime modificationTime;

    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private List<ProductTransaction> productTransactions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<ProductTransaction> getProductTransactions() {
      return productTransactions;
    }

    public void setProductTransactions(List<ProductTransaction> productTransactions) {
      this.productTransactions = productTransactions;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if(product.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", mrp='" + mrp + "'" +
            ", color='" + color + "'" +
            ", creationTime='" + creationTime + "'" +
            ", modificationTime='" + modificationTime + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
