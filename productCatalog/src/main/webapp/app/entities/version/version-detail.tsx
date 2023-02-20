import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './version.reducer';

export const VersionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const versionEntity = useAppSelector(state => state.productcatalog.version.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="versionDetailsHeading">
          <Translate contentKey="productCatalogApp.version.detail.title">Version</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{versionEntity.id}</dd>
          <dt>
            <span id="versionId">
              <Translate contentKey="productCatalogApp.version.versionId">Version Id</Translate>
            </span>
          </dt>
          <dd>{versionEntity.versionId}</dd>
          <dt>
            <span id="versionNumber">
              <Translate contentKey="productCatalogApp.version.versionNumber">Version Number</Translate>
            </span>
          </dt>
          <dd>{versionEntity.versionNumber}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="productCatalogApp.version.active">Active</Translate>
            </span>
          </dt>
          <dd>{versionEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="productCatalogApp.version.cpqQuotedetails">Cpq Quotedetails</Translate>
          </dt>
          <dd>{versionEntity.cpqQuotedetails ? versionEntity.cpqQuotedetails.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/version" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/version/${versionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VersionDetail;
