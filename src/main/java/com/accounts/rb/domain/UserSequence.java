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
    
    public UserSequence() {
      super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "prefix", nullable = false)
    private String prefix;

    @NotNull
    @Column(name = "current_sequence", nullable = false)
    private Integer currentSequence;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(Integer currentSequence) {
        this.currentSequence = currentSequence;
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
    
    public UserSequence(String createdBy, String prefix, Integer currentSequence) {
      super();
      this.createdBy = createdBy;
      this.prefix = prefix;
      this.currentSequence = currentSequence;
    }

    @Override
    public String toString() {
        return "UserSequence{" +
            "id=" + id +
            ", createdBy='" + createdBy + "'" +
            ", prefix='" + prefix + "'" +
            ", currentSequence='" + currentSequence + "'" +
            '}';
    }
}
