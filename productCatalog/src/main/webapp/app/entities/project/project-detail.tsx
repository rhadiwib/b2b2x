import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './project.reducer';

export const ProjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const projectEntity = useAppSelector(state => state.productcatalog.project.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectDetailsHeading">
          <Translate contentKey="productCatalogApp.project.detail.title">Project</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{projectEntity.id}</dd>
          <dt>
            <span id="projectId">
              <Translate contentKey="productCatalogApp.project.projectId">Project Id</Translate>
            </span>
          </dt>
          <dd>{projectEntity.projectId}</dd>
          <dt>
            <span id="projectName">
              <Translate contentKey="productCatalogApp.project.projectName">Project Name</Translate>
            </span>
          </dt>
          <dd>{projectEntity.projectName}</dd>
          <dt>
            <span id="discountTier">
              <Translate contentKey="productCatalogApp.project.discountTier">Discount Tier</Translate>
            </span>
          </dt>
          <dd>{projectEntity.discountTier}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="productCatalogApp.project.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{projectEntity.companyName}</dd>
          <dt>
            <span id="accountManager">
              <Translate contentKey="productCatalogApp.project.accountManager">Account Manager</Translate>
            </span>
          </dt>
          <dd>{projectEntity.accountManager}</dd>
          <dt>
            <span id="estQuantity">
              <Translate contentKey="productCatalogApp.project.estQuantity">Est Quantity</Translate>
            </span>
          </dt>
          <dd>{projectEntity.estQuantity}</dd>
          <dt>
            <span id="contractStartPeriod">
              <Translate contentKey="productCatalogApp.project.contractStartPeriod">Contract Start Period</Translate>
            </span>
          </dt>
          <dd>
            {projectEntity.contractStartPeriod ? (
              <TextFormat value={projectEntity.contractStartPeriod} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="contractEndPeriod">
              <Translate contentKey="productCatalogApp.project.contractEndPeriod">Contract End Period</Translate>
            </span>
          </dt>
          <dd>
            {projectEntity.contractEndPeriod ? (
              <TextFormat value={projectEntity.contractEndPeriod} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/project" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/project/${projectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectDetail;
