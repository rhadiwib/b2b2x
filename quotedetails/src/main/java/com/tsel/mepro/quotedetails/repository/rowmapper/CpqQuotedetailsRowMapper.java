package com.tsel.mepro.quotedetails.repository.rowmapper;

import com.tsel.mepro.quotedetails.domain.CpqQuotedetails;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CpqQuotedetails}, with proper type conversions.
 */
@Service
public class CpqQuotedetailsRowMapper implements BiFunction<Row, String, CpqQuotedetails> {

    private final ColumnConverter converter;

    public CpqQuotedetailsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CpqQuotedetails} stored in the database.
     */
    @Override
    public CpqQuotedetails apply(Row row, String prefix) {
        CpqQuotedetails entity = new CpqQuotedetails();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuoteId(converter.fromRow(row, prefix + "_quote_id", String.class));
        entity.setQuoteStatus(converter.fromRow(row, prefix + "_quote_status", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setProjectId(converter.fromRow(row, prefix + "_project_id", Long.class));
        return entity;
    }
}
