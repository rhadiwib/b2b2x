package com.tsel.mepro.productcatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CpqQuotedetails.
 */
@Table("cpq_quotedetails")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CpqQuotedetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("quote_id")
    private String quoteId;

    @Column("quote_status")
    private String quoteStatus;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Transient
    private Project project;

    @Transient
    @JsonIgnoreProperties(value = { "cpqQuotedetails" }, allowSetters = true)
    private Set<Bundle> bundles = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "cpqQuotedetails" }, allowSetters = true)
    private Set<Version> versions = new HashSet<>();

    @Column("project_id")
    private Long projectId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CpqQuotedetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuoteId() {
        return this.quoteId;
    }

    public CpqQuotedetails quoteId(String quoteId) {
        this.setQuoteId(quoteId);
        return this;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteStatus() {
        return this.quoteStatus;
    }

    public CpqQuotedetails quoteStatus(String quoteStatus) {
        this.setQuoteStatus(quoteStatus);
        return this;
    }

    public void setQuoteStatus(String quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public CpqQuotedetails createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.projectId = project != null ? project.getId() : null;
    }

    public CpqQuotedetails project(Project project) {
        this.setProject(project);
        return this;
    }

    public Set<Bundle> getBundles() {
        return this.bundles;
    }

    public void setBundles(Set<Bundle> bundles) {
        if (this.bundles != null) {
            this.bundles.forEach(i -> i.setCpqQuotedetails(null));
        }
        if (bundles != null) {
            bundles.forEach(i -> i.setCpqQuotedetails(this));
        }
        this.bundles = bundles;
    }

    public CpqQuotedetails bundles(Set<Bundle> bundles) {
        this.setBundles(bundles);
        return this;
    }

    public CpqQuotedetails addBundles(Bundle bundle) {
        this.bundles.add(bundle);
        bundle.setCpqQuotedetails(this);
        return this;
    }

    public CpqQuotedetails removeBundles(Bundle bundle) {
        this.bundles.remove(bundle);
        bundle.setCpqQuotedetails(null);
        return this;
    }

    public Set<Version> getVersions() {
        return this.versions;
    }

    public void setVersions(Set<Version> versions) {
        if (this.versions != null) {
            this.versions.forEach(i -> i.setCpqQuotedetails(null));
        }
        if (versions != null) {
            versions.forEach(i -> i.setCpqQuotedetails(this));
        }
        this.versions = versions;
    }

    public CpqQuotedetails versions(Set<Version> versions) {
        this.setVersions(versions);
        return this;
    }

    public CpqQuotedetails addVersions(Version version) {
        this.versions.add(version);
        version.setCpqQuotedetails(this);
        return this;
    }

    public CpqQuotedetails removeVersions(Version version) {
        this.versions.remove(version);
        version.setCpqQuotedetails(null);
        return this;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long project) {
        this.projectId = project;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CpqQuotedetails)) {
            return false;
        }
        return id != null && id.equals(((CpqQuotedetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CpqQuotedetails{" +
            "id=" + getId() +
            ", quoteId='" + getQuoteId() + "'" +
            ", quoteStatus='" + getQuoteStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
