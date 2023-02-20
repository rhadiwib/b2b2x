package com.tsel.mepro.quotedetails.repository;

import com.tsel.mepro.quotedetails.domain.Version;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Version entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionRepository extends ReactiveCrudRepository<Version, Long>, VersionRepositoryInternal {
    Flux<Version> findAllBy(Pageable pageable);

    @Query("SELECT * FROM version entity WHERE entity.cpq_quotedetails_id = :id")
    Flux<Version> findByCpqQuotedetails(Long id);

    @Query("SELECT * FROM version entity WHERE entity.cpq_quotedetails_id IS NULL")
    Flux<Version> findAllWhereCpqQuotedetailsIsNull();

    @Override
    <S extends Version> Mono<S> save(S entity);

    @Override
    Flux<Version> findAll();

    @Override
    Mono<Version> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface VersionRepositoryInternal {
    <S extends Version> Mono<S> save(S entity);

    Flux<Version> findAllBy(Pageable pageable);

    Flux<Version> findAll();

    Mono<Version> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Version> findAllBy(Pageable pageable, Criteria criteria);

}
