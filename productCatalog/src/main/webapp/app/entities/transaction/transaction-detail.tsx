import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transaction.reducer';

export const TransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transactionEntity = useAppSelector(state => state.productcatalog.transaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transactionDetailsHeading">
          <Translate contentKey="productCatalogApp.transaction.detail.title">Transaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.id}</dd>
          <dt>
            <span id="transactionId">
              <Translate contentKey="productCatalogApp.transaction.transactionId">Transaction Id</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.transactionId}</dd>
          <dt>
            <span id="channel">
              <Translate contentKey="productCatalogApp.transaction.channel">Channel</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.channel}</dd>
          <dt>
            <span id="statusCode">
              <Translate contentKey="productCatalogApp.transaction.statusCode">Status Code</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.statusCode}</dd>
          <dt>
            <span id="statusDesc">
              <Translate contentKey="productCatalogApp.transaction.statusDesc">Status Desc</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.statusDesc}</dd>
        </dl>
        <Button tag={Link} to="/transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transaction/${transactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransactionDetail;
