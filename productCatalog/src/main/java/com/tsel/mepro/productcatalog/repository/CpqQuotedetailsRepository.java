package com.tsel.mepro.productcatalog.repository;

import com.tsel.mepro.productcatalog.domain.CpqQuotedetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CpqQuotedetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CpqQuotedetailsRepository extends ReactiveCrudRepository<CpqQuotedetails, Long>, CpqQuotedetailsRepositoryInternal {
    Flux<CpqQuotedetails> findAllBy(Pageable pageable);

    @Query("SELECT * FROM cpq_quotedetails entity WHERE entity.project_id = :id")
    Flux<CpqQuotedetails> findByProject(Long id);

    @Query("SELECT * FROM cpq_quotedetails entity WHERE entity.project_id IS NULL")
    Flux<CpqQuotedetails> findAllWhereProjectIsNull();

    @Override
    <S extends CpqQuotedetails> Mono<S> save(S entity);

    @Override
    Flux<CpqQuotedetails> findAll();

    @Override
    Mono<CpqQuotedetails> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CpqQuotedetailsRepositoryInternal {
    <S extends CpqQuotedetails> Mono<S> save(S entity);

    Flux<CpqQuotedetails> findAllBy(Pageable pageable);

    Flux<CpqQuotedetails> findAll();

    Mono<CpqQuotedetails> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CpqQuotedetails> findAllBy(Pageable pageable, Criteria criteria);

}
