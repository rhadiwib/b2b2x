package com.tsel.mepro.productcatalog.service.impl;

import com.tsel.mepro.productcatalog.domain.Project;
import com.tsel.mepro.productcatalog.repository.ProjectRepository;
import com.tsel.mepro.productcatalog.service.ProjectService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Mono<Project> save(Project project) {
        log.debug("Request to save Project : {}", project);
        return projectRepository.save(project);
    }

    @Override
    public Mono<Project> update(Project project) {
        log.debug("Request to update Project : {}", project);
        return projectRepository.save(project);
    }

    @Override
    public Mono<Project> partialUpdate(Project project) {
        log.debug("Request to partially update Project : {}", project);

        return projectRepository
            .findById(project.getId())
            .map(existingProject -> {
                if (project.getProjectId() != null) {
                    existingProject.setProjectId(project.getProjectId());
                }
                if (project.getProjectName() != null) {
                    existingProject.setProjectName(project.getProjectName());
                }
                if (project.getDiscountTier() != null) {
                    existingProject.setDiscountTier(project.getDiscountTier());
                }
                if (project.getCompanyName() != null) {
                    existingProject.setCompanyName(project.getCompanyName());
                }
                if (project.getAccountManager() != null) {
                    existingProject.setAccountManager(project.getAccountManager());
                }
                if (project.getEstQuantity() != null) {
                    existingProject.setEstQuantity(project.getEstQuantity());
                }
                if (project.getContractStartPeriod() != null) {
                    existingProject.setContractStartPeriod(project.getContractStartPeriod());
                }
                if (project.getContractEndPeriod() != null) {
                    existingProject.setContractEndPeriod(project.getContractEndPeriod());
                }

                return existingProject;
            })
            .flatMap(projectRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Project> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAllBy(pageable);
    }

    /**
     *  Get all the projects where CpqQuotedetails is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Project> findAllWhereCpqQuotedetailsIsNull() {
        log.debug("Request to get all projects where CpqQuotedetails is null");
        return projectRepository.findAllWhereCpqQuotedetailsIsNull();
    }

    public Mono<Long> countAll() {
        return projectRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Project> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        return projectRepository.deleteById(id);
    }
}
