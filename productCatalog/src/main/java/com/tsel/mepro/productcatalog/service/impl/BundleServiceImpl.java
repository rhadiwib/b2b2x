package com.tsel.mepro.productcatalog.service.impl;

import com.tsel.mepro.productcatalog.domain.Bundle;
import com.tsel.mepro.productcatalog.repository.BundleRepository;
import com.tsel.mepro.productcatalog.service.BundleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Bundle}.
 */
@Service
@Transactional
public class BundleServiceImpl implements BundleService {

    private final Logger log = LoggerFactory.getLogger(BundleServiceImpl.class);

    private final BundleRepository bundleRepository;

    public BundleServiceImpl(BundleRepository bundleRepository) {
        this.bundleRepository = bundleRepository;
    }

    @Override
    public Mono<Bundle> save(Bundle bundle) {
        log.debug("Request to save Bundle : {}", bundle);
        return bundleRepository.save(bundle);
    }

    @Override
    public Mono<Bundle> update(Bundle bundle) {
        log.debug("Request to update Bundle : {}", bundle);
        return bundleRepository.save(bundle);
    }

    @Override
    public Mono<Bundle> partialUpdate(Bundle bundle) {
        log.debug("Request to partially update Bundle : {}", bundle);

        return bundleRepository
            .findById(bundle.getId())
            .map(existingBundle -> {
                if (bundle.getBundleId() != null) {
                    existingBundle.setBundleId(bundle.getBundleId());
                }
                if (bundle.getBundleName() != null) {
                    existingBundle.setBundleName(bundle.getBundleName());
                }
                if (bundle.getQuoteTemplateId() != null) {
                    existingBundle.setQuoteTemplateId(bundle.getQuoteTemplateId());
                }
                if (bundle.getIsCompatible() != null) {
                    existingBundle.setIsCompatible(bundle.getIsCompatible());
                }
                if (bundle.getRecurringAmount() != null) {
                    existingBundle.setRecurringAmount(bundle.getRecurringAmount());
                }
                if (bundle.getSingleAmount() != null) {
                    existingBundle.setSingleAmount(bundle.getSingleAmount());
                }
                if (bundle.getUsageAmount() != null) {
                    existingBundle.setUsageAmount(bundle.getUsageAmount());
                }
                if (bundle.getTotalAmount() != null) {
                    existingBundle.setTotalAmount(bundle.getTotalAmount());
                }
                if (bundle.getCreatedAt() != null) {
                    existingBundle.setCreatedAt(bundle.getCreatedAt());
                }
                if (bundle.getCreatedBy() != null) {
                    existingBundle.setCreatedBy(bundle.getCreatedBy());
                }
                if (bundle.getQuantity() != null) {
                    existingBundle.setQuantity(bundle.getQuantity());
                }

                return existingBundle;
            })
            .flatMap(bundleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Bundle> findAll(Pageable pageable) {
        log.debug("Request to get all Bundles");
        return bundleRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return bundleRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Bundle> findOne(Long id) {
        log.debug("Request to get Bundle : {}", id);
        return bundleRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Bundle : {}", id);
        return bundleRepository.deleteById(id);
    }
}
