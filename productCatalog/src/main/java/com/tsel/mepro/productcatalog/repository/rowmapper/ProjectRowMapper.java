package com.tsel.mepro.productcatalog.repository.rowmapper;

import com.tsel.mepro.productcatalog.domain.Project;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Project}, with proper type conversions.
 */
@Service
public class ProjectRowMapper implements BiFunction<Row, String, Project> {

    private final ColumnConverter converter;

    public ProjectRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Project} stored in the database.
     */
    @Override
    public Project apply(Row row, String prefix) {
        Project entity = new Project();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setProjectId(converter.fromRow(row, prefix + "_project_id", String.class));
        entity.setProjectName(converter.fromRow(row, prefix + "_project_name", String.class));
        entity.setDiscountTier(converter.fromRow(row, prefix + "_discount_tier", String.class));
        entity.setCompanyName(converter.fromRow(row, prefix + "_company_name", String.class));
        entity.setAccountManager(converter.fromRow(row, prefix + "_account_manager", String.class));
        entity.setEstQuantity(converter.fromRow(row, prefix + "_est_quantity", Integer.class));
        entity.setContractStartPeriod(converter.fromRow(row, prefix + "_contract_start_period", ZonedDateTime.class));
        entity.setContractEndPeriod(converter.fromRow(row, prefix + "_contract_end_period", ZonedDateTime.class));
        return entity;
    }
}
