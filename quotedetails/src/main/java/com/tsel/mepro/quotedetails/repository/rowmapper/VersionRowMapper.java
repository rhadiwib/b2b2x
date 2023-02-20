package com.tsel.mepro.quotedetails.repository.rowmapper;

import com.tsel.mepro.quotedetails.domain.Version;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Version}, with proper type conversions.
 */
@Service
public class VersionRowMapper implements BiFunction<Row, String, Version> {

    private final ColumnConverter converter;

    public VersionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Version} stored in the database.
     */
    @Override
    public Version apply(Row row, String prefix) {
        Version entity = new Version();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setVersionId(converter.fromRow(row, prefix + "_version_id", Integer.class));
        entity.setVersionNumber(converter.fromRow(row, prefix + "_version_number", String.class));
        entity.setActive(converter.fromRow(row, prefix + "_active", Boolean.class));
        entity.setCpqQuotedetailsId(converter.fromRow(row, prefix + "_cpq_quotedetails_id", Long.class));
        return entity;
    }
}
