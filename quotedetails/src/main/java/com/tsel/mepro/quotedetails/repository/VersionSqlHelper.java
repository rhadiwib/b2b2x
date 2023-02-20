package com.tsel.mepro.quotedetails.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class VersionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("version_id", table, columnPrefix + "_version_id"));
        columns.add(Column.aliased("version_number", table, columnPrefix + "_version_number"));
        columns.add(Column.aliased("active", table, columnPrefix + "_active"));

        columns.add(Column.aliased("cpq_quotedetails_id", table, columnPrefix + "_cpq_quotedetails_id"));
        return columns;
    }
}
