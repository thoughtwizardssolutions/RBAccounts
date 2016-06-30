package com.accounts.rb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserSequence.
 */
@Entity
@Table(name = "user_sequence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserSequence implements Serializable {

  private static final long serialVersionUID = 1L;

  public UserSequence(String createdBy,String prefixTax, String prefixSales, String prefixSample, Integer taxSequence, Integer salesSequence, Integer sampleInvoiceSequence) {
    super();
    this.createdBy = createdBy;
    this.salesSequence = salesSequence;
    this.sampleSequence = sampleInvoiceSequence;
    this.prefixTax = prefixTax;
    this.prefixSales = prefixSales;
    this.prefixSample = prefixSample;
    this.taxSequence = taxSequence;
  }

    public UserSequence() {
      super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "sales_sequence")
    private Integer salesSequence;

    @Column(name = "sample_sequence")
    private Integer sampleSequence;

    @Column(name = "prefix_tax")
    private String prefixTax;

    @Column(name = "prefix_sales")
    private String prefixSales;

    @Column(name = "prefix_sample")
    private String prefixSample;

    @Column(name = "tax_sequence")
    private Integer taxSequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getSalesSequence() {
        return salesSequence;
    }

    public void setSalesSequence(Integer salesSequence) {
        this.salesSequence = salesSequence;
    }

    public Integer getSampleInvoiceSequence() {
        return sampleSequence;
    }

    public void setSampleInvoiceSequence(Integer sampleInvoiceSequence) {
        this.sampleSequence = sampleInvoiceSequence;
    }

    public Integer getTaxSequence() {
        return taxSequence;
    }

    public void setTaxSequence(Integer taxSequence) {
        this.taxSequence = taxSequence;
    }

    public String getPrefixTax() {
      return prefixTax;
    }

    public void setPrefixTax(String prefixTax) {
      this.prefixTax = prefixTax;
    }

    public String getPrefixSales() {
      return prefixSales;
    }

    public void setPrefixSales(String prefixSales) {
      this.prefixSales = prefixSales;
    }

    public String getPrefixSample() {
      return prefixSample;
    }

    public void setPrefixSample(String prefixSample) {
      this.prefixSample = prefixSample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSequence userSequence = (UserSequence) o;
        if(userSequence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userSequence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserSequence{" +
            "id=" + id +
            ", createdBy='" + createdBy + "'" +
            ", salesSequence='" + salesSequence + "'" +
            ", sampleInvoiceSequence='" + sampleSequence + "'" +
            ", prefixTax='" + prefixTax + "'" +
            ", prefixSales='" + prefixSales + "'" +
            ", prefixSample='" + prefixSample + "'" +
            ", taxSequence='" + taxSequence + "'" +
            '}';
    }
}
