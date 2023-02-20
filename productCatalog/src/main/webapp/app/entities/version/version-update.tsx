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
import { IVersion } from 'app/shared/model/version.model';
import { getEntity, updateEntity, createEntity, reset } from './version.reducer';

export const VersionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cpqQuotedetails = useAppSelector(state => state.productcatalog.cpqQuotedetails.entities);
  const versionEntity = useAppSelector(state => state.productcatalog.version.entity);
  const loading = useAppSelector(state => state.productcatalog.version.loading);
  const updating = useAppSelector(state => state.productcatalog.version.updating);
  const updateSuccess = useAppSelector(state => state.productcatalog.version.updateSuccess);

  const handleClose = () => {
    navigate('/version' + location.search);
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
    const entity = {
      ...versionEntity,
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
      ? {}
      : {
          ...versionEntity,
          cpqQuotedetails: versionEntity?.cpqQuotedetails?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="productCatalogApp.version.home.createOrEditLabel" data-cy="VersionCreateUpdateHeading">
            <Translate contentKey="productCatalogApp.version.home.createOrEditLabel">Create or edit a Version</Translate>
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
                  id="version-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('productCatalogApp.version.versionId')}
                id="version-versionId"
                name="versionId"
                data-cy="versionId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('productCatalogApp.version.versionNumber')}
                id="version-versionNumber"
                name="versionNumber"
                data-cy="versionNumber"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.version.active')}
                id="version-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="version-cpqQuotedetails"
                name="cpqQuotedetails"
                data-cy="cpqQuotedetails"
                label={translate('productCatalogApp.version.cpqQuotedetails')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/version" replace color="info">
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

export default VersionUpdate;
