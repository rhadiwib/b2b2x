package com.tsel.mepro.quotedetails.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.tsel.mepro.quotedetails.domain.Version;
import com.tsel.mepro.quotedetails.repository.rowmapper.CpqQuotedetailsRowMapper;
import com.tsel.mepro.quotedetails.repository.rowmapper.VersionRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Version entity.
 */
@SuppressWarnings("unused")
class VersionRepositoryInternalImpl extends SimpleR2dbcRepository<Version, Long> implements VersionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CpqQuotedetailsRowMapper cpqquotedetailsMapper;
    private final VersionRowMapper versionMapper;

    private static final Table entityTable = Table.aliased("version", EntityManager.ENTITY_ALIAS);
    private static final Table cpqQuotedetailsTable = Table.aliased("cpq_quotedetails", "cpqQuotedetails");

    public VersionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CpqQuotedetailsRowMapper cpqquotedetailsMapper,
        VersionRowMapper versionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Version.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.cpqquotedetailsMapper = cpqquotedetailsMapper;
        this.versionMapper = versionMapper;
    }

    @Override
    public Flux<Version> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Version> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = VersionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CpqQuotedetailsSqlHelper.getColumns(cpqQuotedetailsTable, "cpqQuotedetails"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(cpqQuotedetailsTable)
            .on(Column.create("cpq_quotedetails_id", entityTable))
            .equals(Column.create("id", cpqQuotedetailsTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Version.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Version> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Version> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Version process(Row row, RowMetadata metadata) {
        Version entity = versionMapper.apply(row, "e");
        entity.setCpqQuotedetails(cpqquotedetailsMapper.apply(row, "cpqQuotedetails"));
        return entity;
    }

    @Override
    public <S extends Version> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
