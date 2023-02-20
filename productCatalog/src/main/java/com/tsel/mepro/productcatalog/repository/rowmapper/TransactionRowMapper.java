package com.tsel.mepro.productcatalog.repository.rowmapper;

import com.tsel.mepro.productcatalog.domain.Transaction;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Transaction}, with proper type conversions.
 */
@Service
public class TransactionRowMapper implements BiFunction<Row, String, Transaction> {

    private final ColumnConverter converter;

    public TransactionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Transaction} stored in the database.
     */
    @Override
    public Transaction apply(Row row, String prefix) {
        Transaction entity = new Transaction();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTransactionId(converter.fromRow(row, prefix + "_transaction_id", String.class));
        entity.setChannel(converter.fromRow(row, prefix + "_channel", String.class));
        entity.setStatusCode(converter.fromRow(row, prefix + "_status_code", String.class));
        entity.setStatusDesc(converter.fromRow(row, prefix + "_status_desc", String.class));
        return entity;
    }
}
