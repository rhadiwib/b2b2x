package com.tsel.mepro.quotedetails.service;

import com.tsel.mepro.quotedetails.domain.CpqQuotedetails;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link CpqQuotedetails}.
 */
public interface CpqQuotedetailsService {
    /**
     * Save a cpqQuotedetails.
     *
     * @param cpqQuotedetails the entity to save.
     * @return the persisted entity.
     */
    Mono<CpqQuotedetails> save(CpqQuotedetails cpqQuotedetails);

    /**
     * Updates a cpqQuotedetails.
     *
     * @param cpqQuotedetails the entity to update.
     * @return the persisted entity.
     */
    Mono<CpqQuotedetails> update(CpqQuotedetails cpqQuotedetails);

    /**
     * Partially updates a cpqQuotedetails.
     *
     * @param cpqQuotedetails the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CpqQuotedetails> partialUpdate(CpqQuotedetails cpqQuotedetails);

    /**
     * Get all the cpqQuotedetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CpqQuotedetails> findAll(Pageable pageable);

    /**
     * Returns the number of cpqQuotedetails available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" cpqQuotedetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CpqQuotedetails> findOne(Long id);

    /**
     * Delete the "id" cpqQuotedetails.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
