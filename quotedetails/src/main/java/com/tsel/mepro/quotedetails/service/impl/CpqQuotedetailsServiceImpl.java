package com.tsel.mepro.quotedetails.service.impl;

import com.tsel.mepro.quotedetails.domain.CpqQuotedetails;
import com.tsel.mepro.quotedetails.repository.CpqQuotedetailsRepository;
import com.tsel.mepro.quotedetails.service.CpqQuotedetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CpqQuotedetails}.
 */
@Service
@Transactional
public class CpqQuotedetailsServiceImpl implements CpqQuotedetailsService {

    private final Logger log = LoggerFactory.getLogger(CpqQuotedetailsServiceImpl.class);

    private final CpqQuotedetailsRepository cpqQuotedetailsRepository;

    public CpqQuotedetailsServiceImpl(CpqQuotedetailsRepository cpqQuotedetailsRepository) {
        this.cpqQuotedetailsRepository = cpqQuotedetailsRepository;
    }

    @Override
    public Mono<CpqQuotedetails> save(CpqQuotedetails cpqQuotedetails) {
        log.debug("Request to save CpqQuotedetails : {}", cpqQuotedetails);
        return cpqQuotedetailsRepository.save(cpqQuotedetails);
    }

    @Override
    public Mono<CpqQuotedetails> update(CpqQuotedetails cpqQuotedetails) {
        log.debug("Request to update CpqQuotedetails : {}", cpqQuotedetails);
        return cpqQuotedetailsRepository.save(cpqQuotedetails);
    }

    @Override
    public Mono<CpqQuotedetails> partialUpdate(CpqQuotedetails cpqQuotedetails) {
        log.debug("Request to partially update CpqQuotedetails : {}", cpqQuotedetails);

        return cpqQuotedetailsRepository
            .findById(cpqQuotedetails.getId())
            .map(existingCpqQuotedetails -> {
                if (cpqQuotedetails.getQuoteId() != null) {
                    existingCpqQuotedetails.setQuoteId(cpqQuotedetails.getQuoteId());
                }
                if (cpqQuotedetails.getQuoteStatus() != null) {
                    existingCpqQuotedetails.setQuoteStatus(cpqQuotedetails.getQuoteStatus());
                }
                if (cpqQuotedetails.getCreatedAt() != null) {
                    existingCpqQuotedetails.setCreatedAt(cpqQuotedetails.getCreatedAt());
                }

                return existingCpqQuotedetails;
            })
            .flatMap(cpqQuotedetailsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CpqQuotedetails> findAll(Pageable pageable) {
        log.debug("Request to get all CpqQuotedetails");
        return cpqQuotedetailsRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return cpqQuotedetailsRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CpqQuotedetails> findOne(Long id) {
        log.debug("Request to get CpqQuotedetails : {}", id);
        return cpqQuotedetailsRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CpqQuotedetails : {}", id);
        return cpqQuotedetailsRepository.deleteById(id);
    }
}
