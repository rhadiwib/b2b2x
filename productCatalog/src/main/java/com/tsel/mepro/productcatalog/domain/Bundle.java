package com.tsel.mepro.productcatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Bundle.
 */
@Table("bundle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bundle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("bundle_id")
    private String bundleId;

    @Column("bundle_name")
    private String bundleName;

    @Column("quote_template_id")
    private Integer quoteTemplateId;

    @Column("is_compatible")
    private Boolean isCompatible;

    @Column("recurring_amount")
    private Integer recurringAmount;

    @Column("single_amount")
    private Integer singleAmount;

    @Column("usage_amount")
    private Integer usageAmount;

    @Column("total_amount")
    private Integer totalAmount;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("quantity")
    private Integer quantity;

    @Transient
    @JsonIgnoreProperties(value = { "project", "bundles", "versions" }, allowSetters = true)
    private CpqQuotedetails cpqQuotedetails;

    @Column("cpq_quotedetails_id")
    private Long cpqQuotedetailsId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bundle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBundleId() {
        return this.bundleId;
    }

    public Bundle bundleId(String bundleId) {
        this.setBundleId(bundleId);
        return this;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public Bundle bundleName(String bundleName) {
        this.setBundleName(bundleName);
        return this;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public Integer getQuoteTemplateId() {
        return this.quoteTemplateId;
    }

    public Bundle quoteTemplateId(Integer quoteTemplateId) {
        this.setQuoteTemplateId(quoteTemplateId);
        return this;
    }

    public void setQuoteTemplateId(Integer quoteTemplateId) {
        this.quoteTemplateId = quoteTemplateId;
    }

    public Boolean getIsCompatible() {
        return this.isCompatible;
    }

    public Bundle isCompatible(Boolean isCompatible) {
        this.setIsCompatible(isCompatible);
        return this;
    }

    public void setIsCompatible(Boolean isCompatible) {
        this.isCompatible = isCompatible;
    }

    public Integer getRecurringAmount() {
        return this.recurringAmount;
    }

    public Bundle recurringAmount(Integer recurringAmount) {
        this.setRecurringAmount(recurringAmount);
        return this;
    }

    public void setRecurringAmount(Integer recurringAmount) {
        this.recurringAmount = recurringAmount;
    }

    public Integer getSingleAmount() {
        return this.singleAmount;
    }

    public Bundle singleAmount(Integer singleAmount) {
        this.setSingleAmount(singleAmount);
        return this;
    }

    public void setSingleAmount(Integer singleAmount) {
        this.singleAmount = singleAmount;
    }

    public Integer getUsageAmount() {
        return this.usageAmount;
    }

    public Bundle usageAmount(Integer usageAmount) {
        this.setUsageAmount(usageAmount);
        return this;
    }

    public void setUsageAmount(Integer usageAmount) {
        this.usageAmount = usageAmount;
    }

    public Integer getTotalAmount() {
        return this.totalAmount;
    }

    public Bundle totalAmount(Integer totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Bundle createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Bundle createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Bundle quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CpqQuotedetails getCpqQuotedetails() {
        return this.cpqQuotedetails;
    }

    public void setCpqQuotedetails(CpqQuotedetails cpqQuotedetails) {
        this.cpqQuotedetails = cpqQuotedetails;
        this.cpqQuotedetailsId = cpqQuotedetails != null ? cpqQuotedetails.getId() : null;
    }

    public Bundle cpqQuotedetails(CpqQuotedetails cpqQuotedetails) {
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
        if (!(o instanceof Bundle)) {
            return false;
        }
        return id != null && id.equals(((Bundle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bundle{" +
            "id=" + getId() +
            ", bundleId='" + getBundleId() + "'" +
            ", bundleName='" + getBundleName() + "'" +
            ", quoteTemplateId=" + getQuoteTemplateId() +
            ", isCompatible='" + getIsCompatible() + "'" +
            ", recurringAmount=" + getRecurringAmount() +
            ", singleAmount=" + getSingleAmount() +
            ", usageAmount=" + getUsageAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
