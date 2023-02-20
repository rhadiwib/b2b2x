package com.tsel.mepro.quotedetails.service;

import com.tsel.mepro.quotedetails.domain.Version;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link Version}.
 */
public interface VersionService {
    /**
     * Save a version.
     *
     * @param version the entity to save.
     * @return the persisted entity.
     */
    Mono<Version> save(Version version);

    /**
     * Updates a version.
     *
     * @param version the entity to update.
     * @return the persisted entity.
     */
    Mono<Version> update(Version version);

    /**
     * Partially updates a version.
     *
     * @param version the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Version> partialUpdate(Version version);

    /**
     * Get all the versions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Version> findAll(Pageable pageable);

    /**
     * Returns the number of versions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" version.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Version> findOne(Long id);

    /**
     * Delete the "id" version.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
