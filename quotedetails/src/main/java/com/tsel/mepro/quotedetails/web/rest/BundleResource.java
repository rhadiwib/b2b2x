package com.tsel.mepro.quotedetails.web.rest;

import com.tsel.mepro.quotedetails.domain.Bundle;
import com.tsel.mepro.quotedetails.repository.BundleRepository;
import com.tsel.mepro.quotedetails.service.BundleService;
import com.tsel.mepro.quotedetails.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.tsel.mepro.quotedetails.domain.Bundle}.
 */
@RestController
@RequestMapping("/api")
public class BundleResource {

    private final Logger log = LoggerFactory.getLogger(BundleResource.class);

    private static final String ENTITY_NAME = "quotedetailsBundle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BundleService bundleService;

    private final BundleRepository bundleRepository;

    public BundleResource(BundleService bundleService, BundleRepository bundleRepository) {
        this.bundleService = bundleService;
        this.bundleRepository = bundleRepository;
    }

    /**
     * {@code POST  /bundles} : Create a new bundle.
     *
     * @param bundle the bundle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bundle, or with status {@code 400 (Bad Request)} if the bundle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bundles")
    public Mono<ResponseEntity<Bundle>> createBundle(@Valid @RequestBody Bundle bundle) throws URISyntaxException {
        log.debug("REST request to save Bundle : {}", bundle);
        if (bundle.getId() != null) {
            throw new BadRequestAlertException("A new bundle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bundleService
            .save(bundle)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/bundles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /bundles/:id} : Updates an existing bundle.
     *
     * @param id the id of the bundle to save.
     * @param bundle the bundle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bundle,
     * or with status {@code 400 (Bad Request)} if the bundle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bundle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bundles/{id}")
    public Mono<ResponseEntity<Bundle>> updateBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Bundle bundle
    ) throws URISyntaxException {
        log.debug("REST request to update Bundle : {}, {}", id, bundle);
        if (bundle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bundle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bundleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return bundleService
                    .update(bundle)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /bundles/:id} : Partial updates given fields of an existing bundle, field will ignore if it is null
     *
     * @param id the id of the bundle to save.
     * @param bundle the bundle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bundle,
     * or with status {@code 400 (Bad Request)} if the bundle is not valid,
     * or with status {@code 404 (Not Found)} if the bundle is not found,
     * or with status {@code 500 (Internal Server Error)} if the bundle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bundles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Bundle>> partialUpdateBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bundle bundle
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bundle partially : {}, {}", id, bundle);
        if (bundle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bundle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bundleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Bundle> result = bundleService.partialUpdate(bundle);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /bundles} : get all the bundles.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bundles in body.
     */
    @GetMapping("/bundles")
    public Mono<ResponseEntity<List<Bundle>>> getAllBundles(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Bundles");
        return bundleService
            .countAll()
            .zipWith(bundleService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /bundles/:id} : get the "id" bundle.
     *
     * @param id the id of the bundle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bundle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bundles/{id}")
    public Mono<ResponseEntity<Bundle>> getBundle(@PathVariable Long id) {
        log.debug("REST request to get Bundle : {}", id);
        Mono<Bundle> bundle = bundleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bundle);
    }

    /**
     * {@code DELETE  /bundles/:id} : delete the "id" bundle.
     *
     * @param id the id of the bundle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bundles/{id}")
    public Mono<ResponseEntity<Void>> deleteBundle(@PathVariable Long id) {
        log.debug("REST request to delete Bundle : {}", id);
        return bundleService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
