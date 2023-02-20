package com.tsel.mepro.productcatalog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProjectSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("project_id", table, columnPrefix + "_project_id"));
        columns.add(Column.aliased("project_name", table, columnPrefix + "_project_name"));
        columns.add(Column.aliased("discount_tier", table, columnPrefix + "_discount_tier"));
        columns.add(Column.aliased("company_name", table, columnPrefix + "_company_name"));
        columns.add(Column.aliased("account_manager", table, columnPrefix + "_account_manager"));
        columns.add(Column.aliased("est_quantity", table, columnPrefix + "_est_quantity"));
        columns.add(Column.aliased("contract_start_period", table, columnPrefix + "_contract_start_period"));
        columns.add(Column.aliased("contract_end_period", table, columnPrefix + "_contract_end_period"));

        return columns;
    }
}
