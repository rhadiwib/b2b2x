package com.tsel.mepro.productcatalog.service;

import com.tsel.mepro.productcatalog.domain.Bundle;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link Bundle}.
 */
public interface BundleService {
    /**
     * Save a bundle.
     *
     * @param bundle the entity to save.
     * @return the persisted entity.
     */
    Mono<Bundle> save(Bundle bundle);

    /**
     * Updates a bundle.
     *
     * @param bundle the entity to update.
     * @return the persisted entity.
     */
    Mono<Bundle> update(Bundle bundle);

    /**
     * Partially updates a bundle.
     *
     * @param bundle the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Bundle> partialUpdate(Bundle bundle);

    /**
     * Get all the bundles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Bundle> findAll(Pageable pageable);

    /**
     * Returns the number of bundles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" bundle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Bundle> findOne(Long id);

    /**
     * Delete the "id" bundle.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
