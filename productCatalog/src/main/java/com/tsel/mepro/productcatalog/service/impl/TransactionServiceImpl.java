package com.tsel.mepro.productcatalog.service.impl;

import com.tsel.mepro.productcatalog.domain.Transaction;
import com.tsel.mepro.productcatalog.repository.TransactionRepository;
import com.tsel.mepro.productcatalog.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Mono<Transaction> save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public Mono<Transaction> update(Transaction transaction) {
        log.debug("Request to update Transaction : {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public Mono<Transaction> partialUpdate(Transaction transaction) {
        log.debug("Request to partially update Transaction : {}", transaction);

        return transactionRepository
            .findById(transaction.getId())
            .map(existingTransaction -> {
                if (transaction.getTransactionId() != null) {
                    existingTransaction.setTransactionId(transaction.getTransactionId());
                }
                if (transaction.getChannel() != null) {
                    existingTransaction.setChannel(transaction.getChannel());
                }
                if (transaction.getStatusCode() != null) {
                    existingTransaction.setStatusCode(transaction.getStatusCode());
                }
                if (transaction.getStatusDesc() != null) {
                    existingTransaction.setStatusDesc(transaction.getStatusDesc());
                }

                return existingTransaction;
            })
            .flatMap(transactionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return transactionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Transaction> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        return transactionRepository.deleteById(id);
    }
}
