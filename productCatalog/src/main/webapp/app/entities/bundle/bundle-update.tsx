import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICpqQuotedetails } from 'app/shared/model/cpq-quotedetails.model';
import { getEntities as getCpqQuotedetails } from 'app/entities/cpq-quotedetails/cpq-quotedetails.reducer';
import { IBundle } from 'app/shared/model/bundle.model';
import { getEntity, updateEntity, createEntity, reset } from './bundle.reducer';

export const BundleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cpqQuotedetails = useAppSelector(state => state.productcatalog.cpqQuotedetails.entities);
  const bundleEntity = useAppSelector(state => state.productcatalog.bundle.entity);
  const loading = useAppSelector(state => state.productcatalog.bundle.loading);
  const updating = useAppSelector(state => state.productcatalog.bundle.updating);
  const updateSuccess = useAppSelector(state => state.productcatalog.bundle.updateSuccess);

  const handleClose = () => {
    navigate('/bundle' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCpqQuotedetails({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...bundleEntity,
      ...values,
      cpqQuotedetails: cpqQuotedetails.find(it => it.id.toString() === values.cpqQuotedetails.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
        }
      : {
          ...bundleEntity,
          createdAt: convertDateTimeFromServer(bundleEntity.createdAt),
          cpqQuotedetails: bundleEntity?.cpqQuotedetails?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="productCatalogApp.bundle.home.createOrEditLabel" data-cy="BundleCreateUpdateHeading">
            <Translate contentKey="productCatalogApp.bundle.home.createOrEditLabel">Create or edit a Bundle</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="bundle-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('productCatalogApp.bundle.bundleId')}
                id="bundle-bundleId"
                name="bundleId"
                data-cy="bundleId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.bundleName')}
                id="bundle-bundleName"
                name="bundleName"
                data-cy="bundleName"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.quoteTemplateId')}
                id="bundle-quoteTemplateId"
                name="quoteTemplateId"
                data-cy="quoteTemplateId"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.isCompatible')}
                id="bundle-isCompatible"
                name="isCompatible"
                data-cy="isCompatible"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.recurringAmount')}
                id="bundle-recurringAmount"
                name="recurringAmount"
                data-cy="recurringAmount"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.singleAmount')}
                id="bundle-singleAmount"
                name="singleAmount"
                data-cy="singleAmount"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.usageAmount')}
                id="bundle-usageAmount"
                name="usageAmount"
                data-cy="usageAmount"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.totalAmount')}
                id="bundle-totalAmount"
                name="totalAmount"
                data-cy="totalAmount"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.createdAt')}
                id="bundle-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.createdBy')}
                id="bundle-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.bundle.quantity')}
                id="bundle-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
              />
              <ValidatedField
                id="bundle-cpqQuotedetails"
                name="cpqQuotedetails"
                data-cy="cpqQuotedetails"
                label={translate('productCatalogApp.bundle.cpqQuotedetails')}
                type="select"
              >
                <option value="" key="0" />
                {cpqQuotedetails
                  ? cpqQuotedetails.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bundle" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BundleUpdate;
