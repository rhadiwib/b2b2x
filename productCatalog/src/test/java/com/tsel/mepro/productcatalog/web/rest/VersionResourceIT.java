package com.tsel.mepro.productcatalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.tsel.mepro.productcatalog.IntegrationTest;
import com.tsel.mepro.productcatalog.domain.Version;
import com.tsel.mepro.productcatalog.repository.EntityManager;
import com.tsel.mepro.productcatalog.repository.VersionRepository;
import java.time.Duration;
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
 * Integration tests for the {@link VersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VersionResourceIT {

    private static final Integer DEFAULT_VERSION_ID = 1;
    private static final Integer UPDATED_VERSION_ID = 2;

    private static final String DEFAULT_VERSION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VERSION_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Version version;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Version createEntity(EntityManager em) {
        Version version = new Version().versionId(DEFAULT_VERSION_ID).versionNumber(DEFAULT_VERSION_NUMBER).active(DEFAULT_ACTIVE);
        return version;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Version createUpdatedEntity(EntityManager em) {
        Version version = new Version().versionId(UPDATED_VERSION_ID).versionNumber(UPDATED_VERSION_NUMBER).active(UPDATED_ACTIVE);
        return version;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Version.class).block();
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
        version = createEntity(em);
    }

    @Test
    void createVersion() throws Exception {
        int databaseSizeBeforeCreate = versionRepository.findAll().collectList().block().size();
        // Create the Version
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeCreate + 1);
        Version testVersion = versionList.get(versionList.size() - 1);
        assertThat(testVersion.getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
        assertThat(testVersion.getVersionNumber()).isEqualTo(DEFAULT_VERSION_NUMBER);
        assertThat(testVersion.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    void createVersionWithExistingId() throws Exception {
        // Create the Version with an existing ID
        version.setId(1L);

        int databaseSizeBeforeCreate = versionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkVersionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().collectList().block().size();
        // set the field null
        version.setVersionId(null);

        // Create the Version, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllVersions() {
        // Initialize the database
        versionRepository.save(version).block();

        // Get all the versionList
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
            .value(hasItem(version.getId().intValue()))
            .jsonPath("$.[*].versionId")
            .value(hasItem(DEFAULT_VERSION_ID))
            .jsonPath("$.[*].versionNumber")
            .value(hasItem(DEFAULT_VERSION_NUMBER))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getVersion() {
        // Initialize the database
        versionRepository.save(version).block();

        // Get the version
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, version.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(version.getId().intValue()))
            .jsonPath("$.versionId")
            .value(is(DEFAULT_VERSION_ID))
            .jsonPath("$.versionNumber")
            .value(is(DEFAULT_VERSION_NUMBER))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingVersion() {
        // Get the version
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingVersion() throws Exception {
        // Initialize the database
        versionRepository.save(version).block();

        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();

        // Update the version
        Version updatedVersion = versionRepository.findById(version.getId()).block();
        updatedVersion.versionId(UPDATED_VERSION_ID).versionNumber(UPDATED_VERSION_NUMBER).active(UPDATED_ACTIVE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedVersion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedVersion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
        Version testVersion = versionList.get(versionList.size() - 1);
        assertThat(testVersion.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testVersion.getVersionNumber()).isEqualTo(UPDATED_VERSION_NUMBER);
        assertThat(testVersion.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void putNonExistingVersion() throws Exception {
        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();
        version.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, version.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVersion() throws Exception {
        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();
        version.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVersion() throws Exception {
        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();
        version.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVersionWithPatch() throws Exception {
        // Initialize the database
        versionRepository.save(version).block();

        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();

        // Update the version using partial update
        Version partialUpdatedVersion = new Version();
        partialUpdatedVersion.setId(version.getId());

        partialUpdatedVersion.versionId(UPDATED_VERSION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVersion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVersion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
        Version testVersion = versionList.get(versionList.size() - 1);
        assertThat(testVersion.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testVersion.getVersionNumber()).isEqualTo(DEFAULT_VERSION_NUMBER);
        assertThat(testVersion.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    void fullUpdateVersionWithPatch() throws Exception {
        // Initialize the database
        versionRepository.save(version).block();

        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();

        // Update the version using partial update
        Version partialUpdatedVersion = new Version();
        partialUpdatedVersion.setId(version.getId());

        partialUpdatedVersion.versionId(UPDATED_VERSION_ID).versionNumber(UPDATED_VERSION_NUMBER).active(UPDATED_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVersion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVersion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
        Version testVersion = versionList.get(versionList.size() - 1);
        assertThat(testVersion.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testVersion.getVersionNumber()).isEqualTo(UPDATED_VERSION_NUMBER);
        assertThat(testVersion.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    void patchNonExistingVersion() throws Exception {
        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();
        version.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, version.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVersion() throws Exception {
        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();
        version.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVersion() throws Exception {
        int databaseSizeBeforeUpdate = versionRepository.findAll().collectList().block().size();
        version.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(version))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Version in the database
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVersion() {
        // Initialize the database
        versionRepository.save(version).block();

        int databaseSizeBeforeDelete = versionRepository.findAll().collectList().block().size();

        // Delete the version
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, version.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Version> versionList = versionRepository.findAll().collectList().block();
        assertThat(versionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
