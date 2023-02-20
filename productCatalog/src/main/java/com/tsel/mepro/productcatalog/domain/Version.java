package com.tsel.mepro.productcatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Version.
 */
@Table("version")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Version implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("version_id")
    private Integer versionId;

    @Column("version_number")
    private String versionNumber;

    @Column("active")
    private Boolean active;

    @Transient
    @JsonIgnoreProperties(value = { "project", "bundles", "versions" }, allowSetters = true)
    private CpqQuotedetails cpqQuotedetails;

    @Column("cpq_quotedetails_id")
    private Long cpqQuotedetailsId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Version id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersionId() {
        return this.versionId;
    }

    public Version versionId(Integer versionId) {
        this.setVersionId(versionId);
        return this;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getVersionNumber() {
        return this.versionNumber;
    }

    public Version versionNumber(String versionNumber) {
        this.setVersionNumber(versionNumber);
        return this;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Version active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CpqQuotedetails getCpqQuotedetails() {
        return this.cpqQuotedetails;
    }

    public void setCpqQuotedetails(CpqQuotedetails cpqQuotedetails) {
        this.cpqQuotedetails = cpqQuotedetails;
        this.cpqQuotedetailsId = cpqQuotedetails != null ? cpqQuotedetails.getId() : null;
    }

    public Version cpqQuotedetails(CpqQuotedetails cpqQuotedetails) {
        this.setCpqQuotedetails(cpqQuotedetails);
        return this;
    }

    public Long getCpqQuotedetailsId() {
        return this.cpqQuotedetailsId;
    }

    public void setCpqQuotedetailsId(Long cpqQuotedetails) {
        this.cpqQuotedetailsId = cpqQuotedetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Version)) {
            return false;
        }
        return id != null && id.equals(((Version) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Version{" +
            "id=" + getId() +
            ", versionId=" + getVersionId() +
            ", versionNumber='" + getVersionNumber() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
