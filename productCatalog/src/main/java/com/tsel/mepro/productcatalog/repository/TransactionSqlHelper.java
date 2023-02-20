package com.tsel.mepro.productcatalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TransactionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("transaction_id", table, columnPrefix + "_transaction_id"));
        columns.add(Column.aliased("channel", table, columnPrefix + "_channel"));
        columns.add(Column.aliased("status_code", table, columnPrefix + "_status_code"));
        columns.add(Column.aliased("status_desc", table, columnPrefix + "_status_desc"));

        return columns;
    }
}
