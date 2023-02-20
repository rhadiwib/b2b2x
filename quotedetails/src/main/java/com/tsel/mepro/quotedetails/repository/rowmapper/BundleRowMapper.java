package com.tsel.mepro.quotedetails.repository.rowmapper;

import com.tsel.mepro.quotedetails.domain.Bundle;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Bundle}, with proper type conversions.
 */
@Service
public class BundleRowMapper implements BiFunction<Row, String, Bundle> {

    private final ColumnConverter converter;

    public BundleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Bundle} stored in the database.
     */
    @Override
    public Bundle apply(Row row, String prefix) {
        Bundle entity = new Bundle();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setBundleId(converter.fromRow(row, prefix + "_bundle_id", String.class));
        entity.setBundleName(converter.fromRow(row, prefix + "_bundle_name", String.class));
        entity.setQuoteTemplateId(converter.fromRow(row, prefix + "_quote_template_id", Integer.class));
        entity.setIsCompatible(converter.fromRow(row, prefix + "_is_compatible", Boolean.class));
        entity.setRecurringAmount(converter.fromRow(row, prefix + "_recurring_amount", Integer.class));
        entity.setSingleAmount(converter.fromRow(row, prefix + "_single_amount", Integer.class));
        entity.setUsageAmount(converter.fromRow(row, prefix + "_usage_amount", Integer.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", Integer.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setCpqQuotedetailsId(converter.fromRow(row, prefix + "_cpq_quotedetails_id", Long.class));
        return entity;
    }
}
