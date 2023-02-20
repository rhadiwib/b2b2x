package com.tsel.mepro.productcatalog.web.rest;

import static com.tsel.mepro.productcatalog.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.tsel.mepro.productcatalog.IntegrationTest;
import com.tsel.mepro.productcatalog.domain.CpqQuotedetails;
import com.tsel.mepro.productcatalog.repository.CpqQuotedetailsRepository;
import com.tsel.mepro.productcatalog.repository.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CpqQuotedetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CpqQuotedetailsResourceIT {

    private static final String DEFAULT_QUOTE_ID = "AAAAAAAAAA";
    private static final String UPDATED_QUOTE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_QUOTE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_QUOTE_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/cpq-quotedetails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CpqQuotedetailsRepository cpqQuotedetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CpqQuotedetails cpqQuotedetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CpqQuotedetails createEntity(EntityManager em) {
        CpqQuotedetails cpqQuotedetails = new CpqQuotedetails()
            .quoteId(DEFAULT_QUOTE_ID)
            .quoteStatus(DEFAULT_QUOTE_STATUS)
            .createdAt(DEFAULT_CREATED_AT);
        return cpqQuotedetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CpqQuotedetails createUpdatedEntity(EntityManager em) {
        CpqQuotedetails cpqQuotedetails = new CpqQuotedetails()
            .quoteId(UPDATED_QUOTE_ID)
            .quoteStatus(UPDATED_QUOTE_STATUS)
            .createdAt(UPDATED_CREATED_AT);
        return cpqQuotedetails;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CpqQuotedetails.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        cpqQuotedetails = createEntity(em);
    }

    @Test
    void createCpqQuotedetails() throws Exception {
        int databaseSizeBeforeCreate = cpqQuotedetailsRepository.findAll().collectList().block().size();
        // Create the CpqQuotedetails
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeCreate + 1);
        CpqQuotedetails testCpqQuotedetails = cpqQuotedetailsList.get(cpqQuotedetailsList.size() - 1);
        assertThat(testCpqQuotedetails.getQuoteId()).isEqualTo(DEFAULT_QUOTE_ID);
        assertThat(testCpqQuotedetails.getQuoteStatus()).isEqualTo(DEFAULT_QUOTE_STATUS);
        assertThat(testCpqQuotedetails.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    void createCpqQuotedetailsWithExistingId() throws Exception {
        // Create the CpqQuotedetails with an existing ID
        cpqQuotedetails.setId(1L);

        int databaseSizeBeforeCreate = cpqQuotedetailsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuoteIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cpqQuotedetailsRepository.findAll().collectList().block().size();
        // set the field null
        cpqQuotedetails.setQuoteId(null);

        // Create the CpqQuotedetails, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCpqQuotedetails() {
        // Initialize the database
        cpqQuotedetailsRepository.save(cpqQuotedetails).block();

        // Get all the cpqQuotedetailsList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(cpqQuotedetails.getId().intValue()))
            .jsonPath("$.[*].quoteId")
            .value(hasItem(DEFAULT_QUOTE_ID))
            .jsonPath("$.[*].quoteStatus")
            .value(hasItem(DEFAULT_QUOTE_STATUS))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    void getCpqQuotedetails() {
        // Initialize the database
        cpqQuotedetailsRepository.save(cpqQuotedetails).block();

        // Get the cpqQuotedetails
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cpqQuotedetails.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cpqQuotedetails.getId().intValue()))
            .jsonPath("$.quoteId")
            .value(is(DEFAULT_QUOTE_ID))
            .jsonPath("$.quoteStatus")
            .value(is(DEFAULT_QUOTE_STATUS))
            .jsonPath("$.createdAt")
            .value(is(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    void getNonExistingCpqQuotedetails() {
        // Get the cpqQuotedetails
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCpqQuotedetails() throws Exception {
        // Initialize the database
        cpqQuotedetailsRepository.save(cpqQuotedetails).block();

        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();

        // Update the cpqQuotedetails
        CpqQuotedetails updatedCpqQuotedetails = cpqQuotedetailsRepository.findById(cpqQuotedetails.getId()).block();
        updatedCpqQuotedetails.quoteId(UPDATED_QUOTE_ID).quoteStatus(UPDATED_QUOTE_STATUS).createdAt(UPDATED_CREATED_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCpqQuotedetails.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCpqQuotedetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
        CpqQuotedetails testCpqQuotedetails = cpqQuotedetailsList.get(cpqQuotedetailsList.size() - 1);
        assertThat(testCpqQuotedetails.getQuoteId()).isEqualTo(UPDATED_QUOTE_ID);
        assertThat(testCpqQuotedetails.getQuoteStatus()).isEqualTo(UPDATED_QUOTE_STATUS);
        assertThat(testCpqQuotedetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    void putNonExistingCpqQuotedetails() throws Exception {
        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();
        cpqQuotedetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cpqQuotedetails.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCpqQuotedetails() throws Exception {
        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();
        cpqQuotedetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCpqQuotedetails() throws Exception {
        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();
        cpqQuotedetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCpqQuotedetailsWithPatch() throws Exception {
        // Initialize the database
        cpqQuotedetailsRepository.save(cpqQuotedetails).block();

        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();

        // Update the cpqQuotedetails using partial update
        CpqQuotedetails partialUpdatedCpqQuotedetails = new CpqQuotedetails();
        partialUpdatedCpqQuotedetails.setId(cpqQuotedetails.getId());

        partialUpdatedCpqQuotedetails.createdAt(UPDATED_CREATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCpqQuotedetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCpqQuotedetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
        CpqQuotedetails testCpqQuotedetails = cpqQuotedetailsList.get(cpqQuotedetailsList.size() - 1);
        assertThat(testCpqQuotedetails.getQuoteId()).isEqualTo(DEFAULT_QUOTE_ID);
        assertThat(testCpqQuotedetails.getQuoteStatus()).isEqualTo(DEFAULT_QUOTE_STATUS);
        assertThat(testCpqQuotedetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    void fullUpdateCpqQuotedetailsWithPatch() throws Exception {
        // Initialize the database
        cpqQuotedetailsRepository.save(cpqQuotedetails).block();

        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();

        // Update the cpqQuotedetails using partial update
        CpqQuotedetails partialUpdatedCpqQuotedetails = new CpqQuotedetails();
        partialUpdatedCpqQuotedetails.setId(cpqQuotedetails.getId());

        partialUpdatedCpqQuotedetails.quoteId(UPDATED_QUOTE_ID).quoteStatus(UPDATED_QUOTE_STATUS).createdAt(UPDATED_CREATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCpqQuotedetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCpqQuotedetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
        CpqQuotedetails testCpqQuotedetails = cpqQuotedetailsList.get(cpqQuotedetailsList.size() - 1);
        assertThat(testCpqQuotedetails.getQuoteId()).isEqualTo(UPDATED_QUOTE_ID);
        assertThat(testCpqQuotedetails.getQuoteStatus()).isEqualTo(UPDATED_QUOTE_STATUS);
        assertThat(testCpqQuotedetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    void patchNonExistingCpqQuotedetails() throws Exception {
        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();
        cpqQuotedetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cpqQuotedetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCpqQuotedetails() throws Exception {
        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();
        cpqQuotedetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCpqQuotedetails() throws Exception {
        int databaseSizeBeforeUpdate = cpqQuotedetailsRepository.findAll().collectList().block().size();
        cpqQuotedetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cpqQuotedetails))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CpqQuotedetails in the database
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCpqQuotedetails() {
        // Initialize the database
        cpqQuotedetailsRepository.save(cpqQuotedetails).block();

        int databaseSizeBeforeDelete = cpqQuotedetailsRepository.findAll().collectList().block().size();

        // Delete the cpqQuotedetails
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cpqQuotedetails.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CpqQuotedetails> cpqQuotedetailsList = cpqQuotedetailsRepository.findAll().collectList().block();
        assertThat(cpqQuotedetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
