package com.tsel.mepro.productcatalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CpqQuotedetailsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("quote_id", table, columnPrefix + "_quote_id"));
        columns.add(Column.aliased("quote_status", table, columnPrefix + "_quote_status"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));

        columns.add(Column.aliased("project_id", table, columnPrefix + "_project_id"));
        return columns;
    }
}
