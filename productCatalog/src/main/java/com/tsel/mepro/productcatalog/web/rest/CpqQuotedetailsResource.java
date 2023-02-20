package com.tsel.mepro.productcatalog.web.rest;

import com.tsel.mepro.productcatalog.domain.CpqQuotedetails;
import com.tsel.mepro.productcatalog.repository.CpqQuotedetailsRepository;
import com.tsel.mepro.productcatalog.service.CpqQuotedetailsService;
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
 * REST controller for managing {@link com.tsel.mepro.productcatalog.domain.CpqQuotedetails}.
 */
@RestController
@RequestMapping("/api")
public class CpqQuotedetailsResource {

    private final Logger log = LoggerFactory.getLogger(CpqQuotedetailsResource.class);

    private static final String ENTITY_NAME = "cpqQuotedetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CpqQuotedetailsService cpqQuotedetailsService;

    private final CpqQuotedetailsRepository cpqQuotedetailsRepository;

    public CpqQuotedetailsResource(CpqQuotedetailsService cpqQuotedetailsService, CpqQuotedetailsRepository cpqQuotedetailsRepository) {
        this.cpqQuotedetailsService = cpqQuotedetailsService;
        this.cpqQuotedetailsRepository = cpqQuotedetailsRepository;
    }

    /**
     * {@code POST  /cpq-quotedetails} : Create a new cpqQuotedetails.
     *
     * @param cpqQuotedetails the cpqQuotedetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cpqQuotedetails, or with status {@code 400 (Bad Request)} if the cpqQuotedetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cpq-quotedetails")
    public Mono<ResponseEntity<CpqQuotedetails>> createCpqQuotedetails(@Valid @RequestBody CpqQuotedetails cpqQuotedetails)
        throws URISyntaxException {
        log.debug("REST request to save CpqQuotedetails : {}", cpqQuotedetails);
        if (cpqQuotedetails.getId() != null) {
            throw new BadRequestAlertException("A new cpqQuotedetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cpqQuotedetailsService
            .save(cpqQuotedetails)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cpq-quotedetails/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cpq-quotedetails/:id} : Updates an existing cpqQuotedetails.
     *
     * @param id the id of the cpqQuotedetails to save.
     * @param cpqQuotedetails the cpqQuotedetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cpqQuotedetails,
     * or with status {@code 400 (Bad Request)} if the cpqQuotedetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cpqQuotedetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cpq-quotedetails/{id}")
    public Mono<ResponseEntity<CpqQuotedetails>> updateCpqQuotedetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CpqQuotedetails cpqQuotedetails
    ) throws URISyntaxException {
        log.debug("REST request to update CpqQuotedetails : {}, {}", id, cpqQuotedetails);
        if (cpqQuotedetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cpqQuotedetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cpqQuotedetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cpqQuotedetailsService
                    .update(cpqQuotedetails)
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
     * {@code PATCH  /cpq-quotedetails/:id} : Partial updates given fields of an existing cpqQuotedetails, field will ignore if it is null
     *
     * @param id the id of the cpqQuotedetails to save.
     * @param cpqQuotedetails the cpqQuotedetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cpqQuotedetails,
     * or with status {@code 400 (Bad Request)} if the cpqQuotedetails is not valid,
     * or with status {@code 404 (Not Found)} if the cpqQuotedetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the cpqQuotedetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cpq-quotedetails/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CpqQuotedetails>> partialUpdateCpqQuotedetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CpqQuotedetails cpqQuotedetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update CpqQuotedetails partially : {}, {}", id, cpqQuotedetails);
        if (cpqQuotedetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cpqQuotedetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cpqQuotedetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CpqQuotedetails> result = cpqQuotedetailsService.partialUpdate(cpqQuotedetails);

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
     * {@code GET  /cpq-quotedetails} : get all the cpqQuotedetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cpqQuotedetails in body.
     */
    @GetMapping("/cpq-quotedetails")
    public Mono<ResponseEntity<List<CpqQuotedetails>>> getAllCpqQuotedetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CpqQuotedetails");
        return cpqQuotedetailsService
            .countAll()
            .zipWith(cpqQuotedetailsService.findAll(pageable).collectList())
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
     * {@code GET  /cpq-quotedetails/:id} : get the "id" cpqQuotedetails.
     *
     * @param id the id of the cpqQuotedetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cpqQuotedetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cpq-quotedetails/{id}")
    public Mono<ResponseEntity<CpqQuotedetails>> getCpqQuotedetails(@PathVariable Long id) {
        log.debug("REST request to get CpqQuotedetails : {}", id);
        Mono<CpqQuotedetails> cpqQuotedetails = cpqQuotedetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cpqQuotedetails);
    }

    /**
     * {@code DELETE  /cpq-quotedetails/:id} : delete the "id" cpqQuotedetails.
     *
     * @param id the id of the cpqQuotedetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cpq-quotedetails/{id}")
    public Mono<ResponseEntity<Void>> deleteCpqQuotedetails(@PathVariable Long id) {
        log.debug("REST request to delete CpqQuotedetails : {}", id);
        return cpqQuotedetailsService
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
