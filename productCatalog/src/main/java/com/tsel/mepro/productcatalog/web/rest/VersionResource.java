package com.tsel.mepro.productcatalog.web.rest;

import com.tsel.mepro.productcatalog.domain.Version;
import com.tsel.mepro.productcatalog.repository.VersionRepository;
import com.tsel.mepro.productcatalog.service.VersionService;
import com.tsel.mepro.productcatalog.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.tsel.mepro.productcatalog.domain.Version}.
 */
@RestController
@RequestMapping("/api")
public class VersionResource {

    private final Logger log = LoggerFactory.getLogger(VersionResource.class);

    private static final String ENTITY_NAME = "version";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VersionService versionService;

    private final VersionRepository versionRepository;

    public VersionResource(VersionService versionService, VersionRepository versionRepository) {
        this.versionService = versionService;
        this.versionRepository = versionRepository;
    }

    /**
     * {@code POST  /versions} : Create a new version.
     *
     * @param version the version to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new version, or with status {@code 400 (Bad Request)} if the version has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/versions")
    public Mono<ResponseEntity<Version>> createVersion(@Valid @RequestBody Version version) throws URISyntaxException {
        log.debug("REST request to save Version : {}", version);
        if (version.getId() != null) {
            throw new BadRequestAlertException("A new version cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return versionService
            .save(version)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/versions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /versions/:id} : Updates an existing version.
     *
     * @param id the id of the version to save.
     * @param version the version to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated version,
     * or with status {@code 400 (Bad Request)} if the version is not valid,
     * or with status {@code 500 (Internal Server Error)} if the version couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/versions/{id}")
    public Mono<ResponseEntity<Version>> updateVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Version version
    ) throws URISyntaxException {
        log.debug("REST request to update Version : {}, {}", id, version);
        if (version.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, version.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return versionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return versionService
                    .update(version)
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
     * {@code PATCH  /versions/:id} : Partial updates given fields of an existing version, field will ignore if it is null
     *
     * @param id the id of the version to save.
     * @param version the version to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated version,
     * or with status {@code 400 (Bad Request)} if the version is not valid,
     * or with status {@code 404 (Not Found)} if the version is not found,
     * or with status {@code 500 (Internal Server Error)} if the version couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/versions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Version>> partialUpdateVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Version version
    ) throws URISyntaxException {
        log.debug("REST request to partial update Version partially : {}, {}", id, version);
        if (version.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, version.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return versionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Version> result = versionService.partialUpdate(version);

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
     * {@code GET  /versions} : get all the versions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of versions in body.
     */
    @GetMapping("/versions")
    public Mono<ResponseEntity<List<Version>>> getAllVersions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Versions");
        return versionService
            .countAll()
            .zipWith(versionService.findAll(pageable).collectList())
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
     * {@code GET  /versions/:id} : get the "id" version.
     *
     * @param id the id of the version to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the version, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/versions/{id}")
    public Mono<ResponseEntity<Version>> getVersion(@PathVariable Long id) {
        log.debug("REST request to get Version : {}", id);
        Mono<Version> version = versionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(version);
    }

    /**
     * {@code DELETE  /versions/:id} : delete the "id" version.
     *
     * @param id the id of the version to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/versions/{id}")
    public Mono<ResponseEntity<Void>> deleteVersion(@PathVariable Long id) {
        log.debug("REST request to delete Version : {}", id);
        return versionService
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
