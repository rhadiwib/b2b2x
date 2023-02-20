package com.tsel.mepro.productcatalog.repository;

import com.tsel.mepro.productcatalog.domain.Bundle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Bundle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BundleRepository extends ReactiveCrudRepository<Bundle, Long>, BundleRepositoryInternal {
    Flux<Bundle> findAllBy(Pageable pageable);

    @Query("SELECT * FROM bundle entity WHERE entity.cpq_quotedetails_id = :id")
    Flux<Bundle> findByCpqQuotedetails(Long id);

    @Query("SELECT * FROM bundle entity WHERE entity.cpq_quotedetails_id IS NULL")
    Flux<Bundle> findAllWhereCpqQuotedetailsIsNull();

    @Override
    <S extends Bundle> Mono<S> save(S entity);

    @Override
    Flux<Bundle> findAll();

    @Override
    Mono<Bundle> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BundleRepositoryInternal {
    <S extends Bundle> Mono<S> save(S entity);

    Flux<Bundle> findAllBy(Pageable pageable);

    Flux<Bundle> findAll();

    Mono<Bundle> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Bundle> findAllBy(Pageable pageable, Criteria criteria);

}
