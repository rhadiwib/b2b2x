package com.tsel.mepro.quotedetails.service.impl;

import com.tsel.mepro.quotedetails.domain.Version;
import com.tsel.mepro.quotedetails.repository.VersionRepository;
import com.tsel.mepro.quotedetails.service.VersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Version}.
 */
@Service
@Transactional
public class VersionServiceImpl implements VersionService {

    private final Logger log = LoggerFactory.getLogger(VersionServiceImpl.class);

    private final VersionRepository versionRepository;

    public VersionServiceImpl(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Override
    public Mono<Version> save(Version version) {
        log.debug("Request to save Version : {}", version);
        return versionRepository.save(version);
    }

    @Override
    public Mono<Version> update(Version version) {
        log.debug("Request to update Version : {}", version);
        return versionRepository.save(version);
    }

    @Override
    public Mono<Version> partialUpdate(Version version) {
        log.debug("Request to partially update Version : {}", version);

        return versionRepository
            .findById(version.getId())
            .map(existingVersion -> {
                if (version.getVersionId() != null) {
                    existingVersion.setVersionId(version.getVersionId());
                }
                if (version.getVersionNumber() != null) {
                    existingVersion.setVersionNumber(version.getVersionNumber());
                }
                if (version.getActive() != null) {
                    existingVersion.setActive(version.getActive());
                }

                return existingVersion;
            })
            .flatMap(versionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Version> findAll(Pageable pageable) {
        log.debug("Request to get all Versions");
        return versionRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return versionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Version> findOne(Long id) {
        log.debug("Request to get Version : {}", id);
        return versionRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Version : {}", id);
        return versionRepository.deleteById(id);
    }
}
