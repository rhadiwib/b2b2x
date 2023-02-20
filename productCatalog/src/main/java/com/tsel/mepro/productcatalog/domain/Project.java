package com.tsel.mepro.productcatalog.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Project.
 */
@Table("project")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("project_id")
    private String projectId;

    @Column("project_name")
    private String projectName;

    @Column("discount_tier")
    private String discountTier;

    @Column("company_name")
    private String companyName;

    @Column("account_manager")
    private String accountManager;

    @Column("est_quantity")
    private Integer estQuantity;

    @Column("contract_start_period")
    private ZonedDateTime contractStartPeriod;

    @Column("contract_end_period")
    private ZonedDateTime contractEndPeriod;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public Project projectId(String projectId) {
        this.setProjectId(projectId);
        return this;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Project projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDiscountTier() {
        return this.discountTier;
    }

    public Project discountTier(String discountTier) {
        this.setDiscountTier(discountTier);
        return this;
    }

    public void setDiscountTier(String discountTier) {
        this.discountTier = discountTier;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Project companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountManager() {
        return this.accountManager;
    }

    public Project accountManager(String accountManager) {
        this.setAccountManager(accountManager);
        return this;
    }

    public void setAccountManager(String accountManager) {
        this.accountManager = accountManager;
    }

    public Integer getEstQuantity() {
        return this.estQuantity;
    }

    public Project estQuantity(Integer estQuantity) {
        this.setEstQuantity(estQuantity);
        return this;
    }

    public void setEstQuantity(Integer estQuantity) {
        this.estQuantity = estQuantity;
    }

    public ZonedDateTime getContractStartPeriod() {
        return this.contractStartPeriod;
    }

    public Project contractStartPeriod(ZonedDateTime contractStartPeriod) {
        this.setContractStartPeriod(contractStartPeriod);
        return this;
    }

    public void setContractStartPeriod(ZonedDateTime contractStartPeriod) {
        this.contractStartPeriod = contractStartPeriod;
    }

    public ZonedDateTime getContractEndPeriod() {
        return this.contractEndPeriod;
    }

    public Project contractEndPeriod(ZonedDateTime contractEndPeriod) {
        this.setContractEndPeriod(contractEndPeriod);
        return this;
    }

    public void setContractEndPeriod(ZonedDateTime contractEndPeriod) {
        this.contractEndPeriod = contractEndPeriod;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", projectId='" + getProjectId() + "'" +
            ", projectName='" + getProjectName() + "'" +
            ", discountTier='" + getDiscountTier() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", accountManager='" + getAccountManager() + "'" +
            ", estQuantity=" + getEstQuantity() +
            ", contractStartPeriod='" + getContractStartPeriod() + "'" +
            ", contractEndPeriod='" + getContractEndPeriod() + "'" +
            "}";
    }
}
