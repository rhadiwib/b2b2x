package com.tsel.mepro.productcatalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BundleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("bundle_id", table, columnPrefix + "_bundle_id"));
        columns.add(Column.aliased("bundle_name", table, columnPrefix + "_bundle_name"));
        columns.add(Column.aliased("quote_template_id", table, columnPrefix + "_quote_template_id"));
        columns.add(Column.aliased("is_compatible", table, columnPrefix + "_is_compatible"));
        columns.add(Column.aliased("recurring_amount", table, columnPrefix + "_recurring_amount"));
        columns.add(Column.aliased("single_amount", table, columnPrefix + "_single_amount"));
        columns.add(Column.aliased("usage_amount", table, columnPrefix + "_usage_amount"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));

        columns.add(Column.aliased("cpq_quotedetails_id", table, columnPrefix + "_cpq_quotedetails_id"));
        return columns;
    }
}
