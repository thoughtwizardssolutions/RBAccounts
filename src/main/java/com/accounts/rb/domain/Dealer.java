package com.accounts.rb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Dealer.
 */
@Entity
@Table(name = "dealer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dealer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time")
    private ZonedDateTime creationTime;

    @Column(name = "modification_time")
    private ZonedDateTime modificationTime;

    @NotNull
    @Column(name = "firm_name", nullable = false)
    private String firmName;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "tin")
    private String tin;

    @Column(name = "opening_balance", precision=10, scale=2)
    private BigDecimal openingBalance;

    @Column(name = "current_balance", precision=10, scale=2)
    private BigDecimal currentBalance;

    @Column(name = "terms_and_conditions")
    private String termsAndConditions;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

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

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dealer dealer = (Dealer) o;
        if(dealer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dealer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dealer{" +
            "id=" + id +
            ", creationTime='" + creationTime + "'" +
            ", modificationTime='" + modificationTime + "'" +
            ", firmName='" + firmName + "'" +
            ", ownerName='" + ownerName + "'" +
            ", tin='" + tin + "'" +
            ", openingBalance='" + openingBalance + "'" +
            ", currentBalance='" + currentBalance + "'" +
            ", termsAndConditions='" + termsAndConditions + "'" +
            '}';
    }
}
