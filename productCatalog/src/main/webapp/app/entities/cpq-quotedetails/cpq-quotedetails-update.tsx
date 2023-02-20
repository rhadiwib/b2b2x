import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProject } from 'app/shared/model/project.model';
import { getEntities as getProjects } from 'app/entities/project/project.reducer';
import { ICpqQuotedetails } from 'app/shared/model/cpq-quotedetails.model';
import { getEntity, updateEntity, createEntity, reset } from './cpq-quotedetails.reducer';

export const CpqQuotedetailsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const projects = useAppSelector(state => state.productcatalog.project.entities);
  const cpqQuotedetailsEntity = useAppSelector(state => state.productcatalog.cpqQuotedetails.entity);
  const loading = useAppSelector(state => state.productcatalog.cpqQuotedetails.loading);
  const updating = useAppSelector(state => state.productcatalog.cpqQuotedetails.updating);
  const updateSuccess = useAppSelector(state => state.productcatalog.cpqQuotedetails.updateSuccess);

  const handleClose = () => {
    navigate('/cpq-quotedetails' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...cpqQuotedetailsEntity,
      ...values,
      project: projects.find(it => it.id.toString() === values.project.toString()),
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
          ...cpqQuotedetailsEntity,
          createdAt: convertDateTimeFromServer(cpqQuotedetailsEntity.createdAt),
          project: cpqQuotedetailsEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="productCatalogApp.cpqQuotedetails.home.createOrEditLabel" data-cy="CpqQuotedetailsCreateUpdateHeading">
            <Translate contentKey="productCatalogApp.cpqQuotedetails.home.createOrEditLabel">Create or edit a CpqQuotedetails</Translate>
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
                  id="cpq-quotedetails-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('productCatalogApp.cpqQuotedetails.quoteId')}
                id="cpq-quotedetails-quoteId"
                name="quoteId"
                data-cy="quoteId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('productCatalogApp.cpqQuotedetails.quoteStatus')}
                id="cpq-quotedetails-quoteStatus"
                name="quoteStatus"
                data-cy="quoteStatus"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.cpqQuotedetails.createdAt')}
                id="cpq-quotedetails-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="cpq-quotedetails-project"
                name="project"
                data-cy="project"
                label={translate('productCatalogApp.cpqQuotedetails.project')}
                type="select"
              >
                <option value="" key="0" />
                {projects
                  ? projects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cpq-quotedetails" replace color="info">
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

export default CpqQuotedetailsUpdate;
