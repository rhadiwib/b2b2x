package com.tsel.mepro.quotedetails.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.tsel.mepro.quotedetails.IntegrationTest;
import com.tsel.mepro.quotedetails.domain.Transaction;
import com.tsel.mepro.quotedetails.repository.EntityManager;
import com.tsel.mepro.quotedetails.repository.TransactionRepository;
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
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TransactionResourceIT {

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_DESC = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .transactionId(DEFAULT_TRANSACTION_ID)
            .channel(DEFAULT_CHANNEL)
            .statusCode(DEFAULT_STATUS_CODE)
            .statusDesc(DEFAULT_STATUS_DESC);
        return transaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .transactionId(UPDATED_TRANSACTION_ID)
            .channel(UPDATED_CHANNEL)
            .statusCode(UPDATED_STATUS_CODE)
            .statusDesc(UPDATED_STATUS_DESC);
        return transaction;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Transaction.class).block();
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
        transaction = createEntity(em);
    }

    @Test
    void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().collectList().block().size();
        // Create the Transaction
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testTransaction.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testTransaction.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);
        assertThat(testTransaction.getStatusDesc()).isEqualTo(DEFAULT_STATUS_DESC);
    }

    @Test
    void createTransactionWithExistingId() throws Exception {
        // Create the Transaction with an existing ID
        transaction.setId(1L);

        int databaseSizeBeforeCreate = transactionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().collectList().block().size();
        // set the field null
        transaction.setTransactionId(null);

        // Create the Transaction, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTransactions() {
        // Initialize the database
        transactionRepository.save(transaction).block();

        // Get all the transactionList
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
            .value(hasItem(transaction.getId().intValue()))
            .jsonPath("$.[*].transactionId")
            .value(hasItem(DEFAULT_TRANSACTION_ID))
            .jsonPath("$.[*].channel")
            .value(hasItem(DEFAULT_CHANNEL))
            .jsonPath("$.[*].statusCode")
            .value(hasItem(DEFAULT_STATUS_CODE))
            .jsonPath("$.[*].statusDesc")
            .value(hasItem(DEFAULT_STATUS_DESC));
    }

    @Test
    void getTransaction() {
        // Initialize the database
        transactionRepository.save(transaction).block();

        // Get the transaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(transaction.getId().intValue()))
            .jsonPath("$.transactionId")
            .value(is(DEFAULT_TRANSACTION_ID))
            .jsonPath("$.channel")
            .value(is(DEFAULT_CHANNEL))
            .jsonPath("$.statusCode")
            .value(is(DEFAULT_STATUS_CODE))
            .jsonPath("$.statusDesc")
            .value(is(DEFAULT_STATUS_DESC));
    }

    @Test
    void getNonExistingTransaction() {
        // Get the transaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTransaction() throws Exception {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).block();
        updatedTransaction
            .transactionId(UPDATED_TRANSACTION_ID)
            .channel(UPDATED_CHANNEL)
            .statusCode(UPDATED_STATUS_CODE)
            .statusDesc(UPDATED_STATUS_DESC);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTransaction.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testTransaction.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTransaction.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);
        assertThat(testTransaction.getStatusDesc()).isEqualTo(UPDATED_STATUS_DESC);
    }

    @Test
    void putNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction.channel(UPDATED_CHANNEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testTransaction.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTransaction.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);
        assertThat(testTransaction.getStatusDesc()).isEqualTo(DEFAULT_STATUS_DESC);
    }

    @Test
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .transactionId(UPDATED_TRANSACTION_ID)
            .channel(UPDATED_CHANNEL)
            .statusCode(UPDATED_STATUS_CODE)
            .statusDesc(UPDATED_STATUS_DESC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testTransaction.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTransaction.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);
        assertThat(testTransaction.getStatusDesc()).isEqualTo(UPDATED_STATUS_DESC);
    }

    @Test
    void patchNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTransaction() {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeDelete = transactionRepository.findAll().collectList().block().size();

        // Delete the transaction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
