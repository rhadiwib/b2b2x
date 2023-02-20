import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bundle.reducer';

export const BundleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bundleEntity = useAppSelector(state => state.productcatalog.bundle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bundleDetailsHeading">
          <Translate contentKey="productCatalogApp.bundle.detail.title">Bundle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.id}</dd>
          <dt>
            <span id="bundleId">
              <Translate contentKey="productCatalogApp.bundle.bundleId">Bundle Id</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.bundleId}</dd>
          <dt>
            <span id="bundleName">
              <Translate contentKey="productCatalogApp.bundle.bundleName">Bundle Name</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.bundleName}</dd>
          <dt>
            <span id="quoteTemplateId">
              <Translate contentKey="productCatalogApp.bundle.quoteTemplateId">Quote Template Id</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.quoteTemplateId}</dd>
          <dt>
            <span id="isCompatible">
              <Translate contentKey="productCatalogApp.bundle.isCompatible">Is Compatible</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.isCompatible ? 'true' : 'false'}</dd>
          <dt>
            <span id="recurringAmount">
              <Translate contentKey="productCatalogApp.bundle.recurringAmount">Recurring Amount</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.recurringAmount}</dd>
          <dt>
            <span id="singleAmount">
              <Translate contentKey="productCatalogApp.bundle.singleAmount">Single Amount</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.singleAmount}</dd>
          <dt>
            <span id="usageAmount">
              <Translate contentKey="productCatalogApp.bundle.usageAmount">Usage Amount</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.usageAmount}</dd>
          <dt>
            <span id="totalAmount">
              <Translate contentKey="productCatalogApp.bundle.totalAmount">Total Amount</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.totalAmount}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="productCatalogApp.bundle.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.createdAt ? <TextFormat value={bundleEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="productCatalogApp.bundle.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.createdBy}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="productCatalogApp.bundle.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{bundleEntity.quantity}</dd>
          <dt>
            <Translate contentKey="productCatalogApp.bundle.cpqQuotedetails">Cpq Quotedetails</Translate>
          </dt>
          <dd>{bundleEntity.cpqQuotedetails ? bundleEntity.cpqQuotedetails.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bundle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bundle/${bundleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BundleDetail;
