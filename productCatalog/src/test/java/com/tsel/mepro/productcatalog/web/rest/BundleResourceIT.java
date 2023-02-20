package com.tsel.mepro.productcatalog.web.rest;

import static com.tsel.mepro.productcatalog.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.tsel.mepro.productcatalog.IntegrationTest;
import com.tsel.mepro.productcatalog.domain.Bundle;
import com.tsel.mepro.productcatalog.repository.BundleRepository;
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
 * Integration tests for the {@link BundleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BundleResourceIT {

    private static final String DEFAULT_BUNDLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_BUNDLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BUNDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUNDLE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUOTE_TEMPLATE_ID = 1;
    private static final Integer UPDATED_QUOTE_TEMPLATE_ID = 2;

    private static final Boolean DEFAULT_IS_COMPATIBLE = false;
    private static final Boolean UPDATED_IS_COMPATIBLE = true;

    private static final Integer DEFAULT_RECURRING_AMOUNT = 1;
    private static final Integer UPDATED_RECURRING_AMOUNT = 2;

    private static final Integer DEFAULT_SINGLE_AMOUNT = 1;
    private static final Integer UPDATED_SINGLE_AMOUNT = 2;

    private static final Integer DEFAULT_USAGE_AMOUNT = 1;
    private static final Integer UPDATED_USAGE_AMOUNT = 2;

    private static final Integer DEFAULT_TOTAL_AMOUNT = 1;
    private static final Integer UPDATED_TOTAL_AMOUNT = 2;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/bundles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Bundle bundle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bundle createEntity(EntityManager em) {
        Bundle bundle = new Bundle()
            .bundleId(DEFAULT_BUNDLE_ID)
            .bundleName(DEFAULT_BUNDLE_NAME)
            .quoteTemplateId(DEFAULT_QUOTE_TEMPLATE_ID)
            .isCompatible(DEFAULT_IS_COMPATIBLE)
            .recurringAmount(DEFAULT_RECURRING_AMOUNT)
            .singleAmount(DEFAULT_SINGLE_AMOUNT)
            .usageAmount(DEFAULT_USAGE_AMOUNT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .quantity(DEFAULT_QUANTITY);
        return bundle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bundle createUpdatedEntity(EntityManager em) {
        Bundle bundle = new Bundle()
            .bundleId(UPDATED_BUNDLE_ID)
            .bundleName(UPDATED_BUNDLE_NAME)
            .quoteTemplateId(UPDATED_QUOTE_TEMPLATE_ID)
            .isCompatible(UPDATED_IS_COMPATIBLE)
            .recurringAmount(UPDATED_RECURRING_AMOUNT)
            .singleAmount(UPDATED_SINGLE_AMOUNT)
            .usageAmount(UPDATED_USAGE_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .quantity(UPDATED_QUANTITY);
        return bundle;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Bundle.class).block();
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
        bundle = createEntity(em);
    }

    @Test
    void createBundle() throws Exception {
        int databaseSizeBeforeCreate = bundleRepository.findAll().collectList().block().size();
        // Create the Bundle
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeCreate + 1);
        Bundle testBundle = bundleList.get(bundleList.size() - 1);
        assertThat(testBundle.getBundleId()).isEqualTo(DEFAULT_BUNDLE_ID);
        assertThat(testBundle.getBundleName()).isEqualTo(DEFAULT_BUNDLE_NAME);
        assertThat(testBundle.getQuoteTemplateId()).isEqualTo(DEFAULT_QUOTE_TEMPLATE_ID);
        assertThat(testBundle.getIsCompatible()).isEqualTo(DEFAULT_IS_COMPATIBLE);
        assertThat(testBundle.getRecurringAmount()).isEqualTo(DEFAULT_RECURRING_AMOUNT);
        assertThat(testBundle.getSingleAmount()).isEqualTo(DEFAULT_SINGLE_AMOUNT);
        assertThat(testBundle.getUsageAmount()).isEqualTo(DEFAULT_USAGE_AMOUNT);
        assertThat(testBundle.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testBundle.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testBundle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBundle.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    void createBundleWithExistingId() throws Exception {
        // Create the Bundle with an existing ID
        bundle.setId(1L);

        int databaseSizeBeforeCreate = bundleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBundleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bundleRepository.findAll().collectList().block().size();
        // set the field null
        bundle.setBundleId(null);

        // Create the Bundle, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBundles() {
        // Initialize the database
        bundleRepository.save(bundle).block();

        // Get all the bundleList
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
            .value(hasItem(bundle.getId().intValue()))
            .jsonPath("$.[*].bundleId")
            .value(hasItem(DEFAULT_BUNDLE_ID))
            .jsonPath("$.[*].bundleName")
            .value(hasItem(DEFAULT_BUNDLE_NAME))
            .jsonPath("$.[*].quoteTemplateId")
            .value(hasItem(DEFAULT_QUOTE_TEMPLATE_ID))
            .jsonPath("$.[*].isCompatible")
            .value(hasItem(DEFAULT_IS_COMPATIBLE.booleanValue()))
            .jsonPath("$.[*].recurringAmount")
            .value(hasItem(DEFAULT_RECURRING_AMOUNT))
            .jsonPath("$.[*].singleAmount")
            .value(hasItem(DEFAULT_SINGLE_AMOUNT))
            .jsonPath("$.[*].usageAmount")
            .value(hasItem(DEFAULT_USAGE_AMOUNT))
            .jsonPath("$.[*].totalAmount")
            .value(hasItem(DEFAULT_TOTAL_AMOUNT))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(sameInstant(DEFAULT_CREATED_AT)))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].quantity")
            .value(hasItem(DEFAULT_QUANTITY));
    }

    @Test
    void getBundle() {
        // Initialize the database
        bundleRepository.save(bundle).block();

        // Get the bundle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, bundle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(bundle.getId().intValue()))
            .jsonPath("$.bundleId")
            .value(is(DEFAULT_BUNDLE_ID))
            .jsonPath("$.bundleName")
            .value(is(DEFAULT_BUNDLE_NAME))
            .jsonPath("$.quoteTemplateId")
            .value(is(DEFAULT_QUOTE_TEMPLATE_ID))
            .jsonPath("$.isCompatible")
            .value(is(DEFAULT_IS_COMPATIBLE.booleanValue()))
            .jsonPath("$.recurringAmount")
            .value(is(DEFAULT_RECURRING_AMOUNT))
            .jsonPath("$.singleAmount")
            .value(is(DEFAULT_SINGLE_AMOUNT))
            .jsonPath("$.usageAmount")
            .value(is(DEFAULT_USAGE_AMOUNT))
            .jsonPath("$.totalAmount")
            .value(is(DEFAULT_TOTAL_AMOUNT))
            .jsonPath("$.createdAt")
            .value(is(sameInstant(DEFAULT_CREATED_AT)))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.quantity")
            .value(is(DEFAULT_QUANTITY));
    }

    @Test
    void getNonExistingBundle() {
        // Get the bundle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBundle() throws Exception {
        // Initialize the database
        bundleRepository.save(bundle).block();

        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();

        // Update the bundle
        Bundle updatedBundle = bundleRepository.findById(bundle.getId()).block();
        updatedBundle
            .bundleId(UPDATED_BUNDLE_ID)
            .bundleName(UPDATED_BUNDLE_NAME)
            .quoteTemplateId(UPDATED_QUOTE_TEMPLATE_ID)
            .isCompatible(UPDATED_IS_COMPATIBLE)
            .recurringAmount(UPDATED_RECURRING_AMOUNT)
            .singleAmount(UPDATED_SINGLE_AMOUNT)
            .usageAmount(UPDATED_USAGE_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .quantity(UPDATED_QUANTITY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedBundle.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedBundle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
        Bundle testBundle = bundleList.get(bundleList.size() - 1);
        assertThat(testBundle.getBundleId()).isEqualTo(UPDATED_BUNDLE_ID);
        assertThat(testBundle.getBundleName()).isEqualTo(UPDATED_BUNDLE_NAME);
        assertThat(testBundle.getQuoteTemplateId()).isEqualTo(UPDATED_QUOTE_TEMPLATE_ID);
        assertThat(testBundle.getIsCompatible()).isEqualTo(UPDATED_IS_COMPATIBLE);
        assertThat(testBundle.getRecurringAmount()).isEqualTo(UPDATED_RECURRING_AMOUNT);
        assertThat(testBundle.getSingleAmount()).isEqualTo(UPDATED_SINGLE_AMOUNT);
        assertThat(testBundle.getUsageAmount()).isEqualTo(UPDATED_USAGE_AMOUNT);
        assertThat(testBundle.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testBundle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBundle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBundle.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void putNonExistingBundle() throws Exception {
        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();
        bundle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bundle.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBundle() throws Exception {
        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();
        bundle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBundle() throws Exception {
        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();
        bundle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBundleWithPatch() throws Exception {
        // Initialize the database
        bundleRepository.save(bundle).block();

        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();

        // Update the bundle using partial update
        Bundle partialUpdatedBundle = new Bundle();
        partialUpdatedBundle.setId(bundle.getId());

        partialUpdatedBundle
            .quoteTemplateId(UPDATED_QUOTE_TEMPLATE_ID)
            .singleAmount(UPDATED_SINGLE_AMOUNT)
            .usageAmount(UPDATED_USAGE_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .quantity(UPDATED_QUANTITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBundle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBundle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
        Bundle testBundle = bundleList.get(bundleList.size() - 1);
        assertThat(testBundle.getBundleId()).isEqualTo(DEFAULT_BUNDLE_ID);
        assertThat(testBundle.getBundleName()).isEqualTo(DEFAULT_BUNDLE_NAME);
        assertThat(testBundle.getQuoteTemplateId()).isEqualTo(UPDATED_QUOTE_TEMPLATE_ID);
        assertThat(testBundle.getIsCompatible()).isEqualTo(DEFAULT_IS_COMPATIBLE);
        assertThat(testBundle.getRecurringAmount()).isEqualTo(DEFAULT_RECURRING_AMOUNT);
        assertThat(testBundle.getSingleAmount()).isEqualTo(UPDATED_SINGLE_AMOUNT);
        assertThat(testBundle.getUsageAmount()).isEqualTo(UPDATED_USAGE_AMOUNT);
        assertThat(testBundle.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testBundle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBundle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBundle.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void fullUpdateBundleWithPatch() throws Exception {
        // Initialize the database
        bundleRepository.save(bundle).block();

        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();

        // Update the bundle using partial update
        Bundle partialUpdatedBundle = new Bundle();
        partialUpdatedBundle.setId(bundle.getId());

        partialUpdatedBundle
            .bundleId(UPDATED_BUNDLE_ID)
            .bundleName(UPDATED_BUNDLE_NAME)
            .quoteTemplateId(UPDATED_QUOTE_TEMPLATE_ID)
            .isCompatible(UPDATED_IS_COMPATIBLE)
            .recurringAmount(UPDATED_RECURRING_AMOUNT)
            .singleAmount(UPDATED_SINGLE_AMOUNT)
            .usageAmount(UPDATED_USAGE_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .quantity(UPDATED_QUANTITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBundle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBundle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
        Bundle testBundle = bundleList.get(bundleList.size() - 1);
        assertThat(testBundle.getBundleId()).isEqualTo(UPDATED_BUNDLE_ID);
        assertThat(testBundle.getBundleName()).isEqualTo(UPDATED_BUNDLE_NAME);
        assertThat(testBundle.getQuoteTemplateId()).isEqualTo(UPDATED_QUOTE_TEMPLATE_ID);
        assertThat(testBundle.getIsCompatible()).isEqualTo(UPDATED_IS_COMPATIBLE);
        assertThat(testBundle.getRecurringAmount()).isEqualTo(UPDATED_RECURRING_AMOUNT);
        assertThat(testBundle.getSingleAmount()).isEqualTo(UPDATED_SINGLE_AMOUNT);
        assertThat(testBundle.getUsageAmount()).isEqualTo(UPDATED_USAGE_AMOUNT);
        assertThat(testBundle.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testBundle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBundle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBundle.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void patchNonExistingBundle() throws Exception {
        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();
        bundle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, bundle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBundle() throws Exception {
        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();
        bundle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBundle() throws Exception {
        int databaseSizeBeforeUpdate = bundleRepository.findAll().collectList().block().size();
        bundle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bundle))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Bundle in the database
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBundle() {
        // Initialize the database
        bundleRepository.save(bundle).block();

        int databaseSizeBeforeDelete = bundleRepository.findAll().collectList().block().size();

        // Delete the bundle
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, bundle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Bundle> bundleList = bundleRepository.findAll().collectList().block();
        assertThat(bundleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
