import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cpq-quotedetails.reducer';

export const CpqQuotedetailsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cpqQuotedetailsEntity = useAppSelector(state => state.productcatalog.cpqQuotedetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cpqQuotedetailsDetailsHeading">
          <Translate contentKey="productCatalogApp.cpqQuotedetails.detail.title">CpqQuotedetails</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cpqQuotedetailsEntity.id}</dd>
          <dt>
            <span id="quoteId">
              <Translate contentKey="productCatalogApp.cpqQuotedetails.quoteId">Quote Id</Translate>
            </span>
          </dt>
          <dd>{cpqQuotedetailsEntity.quoteId}</dd>
          <dt>
            <span id="quoteStatus">
              <Translate contentKey="productCatalogApp.cpqQuotedetails.quoteStatus">Quote Status</Translate>
            </span>
          </dt>
          <dd>{cpqQuotedetailsEntity.quoteStatus}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="productCatalogApp.cpqQuotedetails.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {cpqQuotedetailsEntity.createdAt ? (
              <TextFormat value={cpqQuotedetailsEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="productCatalogApp.cpqQuotedetails.project">Project</Translate>
          </dt>
          <dd>{cpqQuotedetailsEntity.project ? cpqQuotedetailsEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/cpq-quotedetails" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cpq-quotedetails/${cpqQuotedetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CpqQuotedetailsDetail;
