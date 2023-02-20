package com.tsel.mepro.productcatalog.web.rest;

import static com.tsel.mepro.productcatalog.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.tsel.mepro.productcatalog.IntegrationTest;
import com.tsel.mepro.productcatalog.domain.Project;
import com.tsel.mepro.productcatalog.repository.EntityManager;
import com.tsel.mepro.productcatalog.repository.ProjectRepository;
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
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProjectResourceIT {

    private static final String DEFAULT_PROJECT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISCOUNT_TIER = "AAAAAAAAAA";
    private static final String UPDATED_DISCOUNT_TIER = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_MANAGER = "BBBBBBBBBB";

    private static final Integer DEFAULT_EST_QUANTITY = 1;
    private static final Integer UPDATED_EST_QUANTITY = 2;

    private static final ZonedDateTime DEFAULT_CONTRACT_START_PERIOD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_START_PERIOD = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CONTRACT_END_PERIOD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_END_PERIOD = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .projectId(DEFAULT_PROJECT_ID)
            .projectName(DEFAULT_PROJECT_NAME)
            .discountTier(DEFAULT_DISCOUNT_TIER)
            .companyName(DEFAULT_COMPANY_NAME)
            .accountManager(DEFAULT_ACCOUNT_MANAGER)
            .estQuantity(DEFAULT_EST_QUANTITY)
            .contractStartPeriod(DEFAULT_CONTRACT_START_PERIOD)
            .contractEndPeriod(DEFAULT_CONTRACT_END_PERIOD);
        return project;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .projectId(UPDATED_PROJECT_ID)
            .projectName(UPDATED_PROJECT_NAME)
            .discountTier(UPDATED_DISCOUNT_TIER)
            .companyName(UPDATED_COMPANY_NAME)
            .accountManager(UPDATED_ACCOUNT_MANAGER)
            .estQuantity(UPDATED_EST_QUANTITY)
            .contractStartPeriod(UPDATED_CONTRACT_START_PERIOD)
            .contractEndPeriod(UPDATED_CONTRACT_END_PERIOD);
        return project;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Project.class).block();
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
        project = createEntity(em);
    }

    @Test
    void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().collectList().block().size();
        // Create the Project
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testProject.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testProject.getDiscountTier()).isEqualTo(DEFAULT_DISCOUNT_TIER);
        assertThat(testProject.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testProject.getAccountManager()).isEqualTo(DEFAULT_ACCOUNT_MANAGER);
        assertThat(testProject.getEstQuantity()).isEqualTo(DEFAULT_EST_QUANTITY);
        assertThat(testProject.getContractStartPeriod()).isEqualTo(DEFAULT_CONTRACT_START_PERIOD);
        assertThat(testProject.getContractEndPeriod()).isEqualTo(DEFAULT_CONTRACT_END_PERIOD);
    }

    @Test
    void createProjectWithExistingId() throws Exception {
        // Create the Project with an existing ID
        project.setId(1L);

        int databaseSizeBeforeCreate = projectRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().collectList().block().size();
        // set the field null
        project.setProjectId(null);

        // Create the Project, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProjects() {
        // Initialize the database
        projectRepository.save(project).block();

        // Get all the projectList
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
            .value(hasItem(project.getId().intValue()))
            .jsonPath("$.[*].projectId")
            .value(hasItem(DEFAULT_PROJECT_ID))
            .jsonPath("$.[*].projectName")
            .value(hasItem(DEFAULT_PROJECT_NAME))
            .jsonPath("$.[*].discountTier")
            .value(hasItem(DEFAULT_DISCOUNT_TIER))
            .jsonPath("$.[*].companyName")
            .value(hasItem(DEFAULT_COMPANY_NAME))
            .jsonPath("$.[*].accountManager")
            .value(hasItem(DEFAULT_ACCOUNT_MANAGER))
            .jsonPath("$.[*].estQuantity")
            .value(hasItem(DEFAULT_EST_QUANTITY))
            .jsonPath("$.[*].contractStartPeriod")
            .value(hasItem(sameInstant(DEFAULT_CONTRACT_START_PERIOD)))
            .jsonPath("$.[*].contractEndPeriod")
            .value(hasItem(sameInstant(DEFAULT_CONTRACT_END_PERIOD)));
    }

    @Test
    void getProject() {
        // Initialize the database
        projectRepository.save(project).block();

        // Get the project
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, project.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(project.getId().intValue()))
            .jsonPath("$.projectId")
            .value(is(DEFAULT_PROJECT_ID))
            .jsonPath("$.projectName")
            .value(is(DEFAULT_PROJECT_NAME))
            .jsonPath("$.discountTier")
            .value(is(DEFAULT_DISCOUNT_TIER))
            .jsonPath("$.companyName")
            .value(is(DEFAULT_COMPANY_NAME))
            .jsonPath("$.accountManager")
            .value(is(DEFAULT_ACCOUNT_MANAGER))
            .jsonPath("$.estQuantity")
            .value(is(DEFAULT_EST_QUANTITY))
            .jsonPath("$.contractStartPeriod")
            .value(is(sameInstant(DEFAULT_CONTRACT_START_PERIOD)))
            .jsonPath("$.contractEndPeriod")
            .value(is(sameInstant(DEFAULT_CONTRACT_END_PERIOD)));
    }

    @Test
    void getNonExistingProject() {
        // Get the project
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProject() throws Exception {
        // Initialize the database
        projectRepository.save(project).block();

        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).block();
        updatedProject
            .projectId(UPDATED_PROJECT_ID)
            .projectName(UPDATED_PROJECT_NAME)
            .discountTier(UPDATED_DISCOUNT_TIER)
            .companyName(UPDATED_COMPANY_NAME)
            .accountManager(UPDATED_ACCOUNT_MANAGER)
            .estQuantity(UPDATED_EST_QUANTITY)
            .contractStartPeriod(UPDATED_CONTRACT_START_PERIOD)
            .contractEndPeriod(UPDATED_CONTRACT_END_PERIOD);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProject.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProject))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testProject.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProject.getDiscountTier()).isEqualTo(UPDATED_DISCOUNT_TIER);
        assertThat(testProject.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testProject.getAccountManager()).isEqualTo(UPDATED_ACCOUNT_MANAGER);
        assertThat(testProject.getEstQuantity()).isEqualTo(UPDATED_EST_QUANTITY);
        assertThat(testProject.getContractStartPeriod()).isEqualTo(UPDATED_CONTRACT_START_PERIOD);
        assertThat(testProject.getContractEndPeriod()).isEqualTo(UPDATED_CONTRACT_END_PERIOD);
    }

    @Test
    void putNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, project.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.save(project).block();

        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .projectId(UPDATED_PROJECT_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .accountManager(UPDATED_ACCOUNT_MANAGER)
            .contractStartPeriod(UPDATED_CONTRACT_START_PERIOD)
            .contractEndPeriod(UPDATED_CONTRACT_END_PERIOD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProject.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testProject.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testProject.getDiscountTier()).isEqualTo(DEFAULT_DISCOUNT_TIER);
        assertThat(testProject.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testProject.getAccountManager()).isEqualTo(UPDATED_ACCOUNT_MANAGER);
        assertThat(testProject.getEstQuantity()).isEqualTo(DEFAULT_EST_QUANTITY);
        assertThat(testProject.getContractStartPeriod()).isEqualTo(UPDATED_CONTRACT_START_PERIOD);
        assertThat(testProject.getContractEndPeriod()).isEqualTo(UPDATED_CONTRACT_END_PERIOD);
    }

    @Test
    void fullUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.save(project).block();

        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .projectId(UPDATED_PROJECT_ID)
            .projectName(UPDATED_PROJECT_NAME)
            .discountTier(UPDATED_DISCOUNT_TIER)
            .companyName(UPDATED_COMPANY_NAME)
            .accountManager(UPDATED_ACCOUNT_MANAGER)
            .estQuantity(UPDATED_EST_QUANTITY)
            .contractStartPeriod(UPDATED_CONTRACT_START_PERIOD)
            .contractEndPeriod(UPDATED_CONTRACT_END_PERIOD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProject.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testProject.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProject.getDiscountTier()).isEqualTo(UPDATED_DISCOUNT_TIER);
        assertThat(testProject.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testProject.getAccountManager()).isEqualTo(UPDATED_ACCOUNT_MANAGER);
        assertThat(testProject.getEstQuantity()).isEqualTo(UPDATED_EST_QUANTITY);
        assertThat(testProject.getContractStartPeriod()).isEqualTo(UPDATED_CONTRACT_START_PERIOD);
        assertThat(testProject.getContractEndPeriod()).isEqualTo(UPDATED_CONTRACT_END_PERIOD);
    }

    @Test
    void patchNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, project.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().collectList().block().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(project))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProject() {
        // Initialize the database
        projectRepository.save(project).block();

        int databaseSizeBeforeDelete = projectRepository.findAll().collectList().block().size();

        // Delete the project
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, project.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll().collectList().block();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
