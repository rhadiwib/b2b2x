import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProject } from 'app/shared/model/project.model';
import { getEntity, updateEntity, createEntity, reset } from './project.reducer';

export const ProjectUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const projectEntity = useAppSelector(state => state.productcatalog.project.entity);
  const loading = useAppSelector(state => state.productcatalog.project.loading);
  const updating = useAppSelector(state => state.productcatalog.project.updating);
  const updateSuccess = useAppSelector(state => state.productcatalog.project.updateSuccess);

  const handleClose = () => {
    navigate('/project' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.contractStartPeriod = convertDateTimeToServer(values.contractStartPeriod);
    values.contractEndPeriod = convertDateTimeToServer(values.contractEndPeriod);

    const entity = {
      ...projectEntity,
      ...values,
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
          contractStartPeriod: displayDefaultDateTime(),
          contractEndPeriod: displayDefaultDateTime(),
        }
      : {
          ...projectEntity,
          contractStartPeriod: convertDateTimeFromServer(projectEntity.contractStartPeriod),
          contractEndPeriod: convertDateTimeFromServer(projectEntity.contractEndPeriod),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="productCatalogApp.project.home.createOrEditLabel" data-cy="ProjectCreateUpdateHeading">
            <Translate contentKey="productCatalogApp.project.home.createOrEditLabel">Create or edit a Project</Translate>
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
                  id="project-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('productCatalogApp.project.projectId')}
                id="project-projectId"
                name="projectId"
                data-cy="projectId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('productCatalogApp.project.projectName')}
                id="project-projectName"
                name="projectName"
                data-cy="projectName"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.project.discountTier')}
                id="project-discountTier"
                name="discountTier"
                data-cy="discountTier"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.project.companyName')}
                id="project-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.project.accountManager')}
                id="project-accountManager"
                name="accountManager"
                data-cy="accountManager"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.project.estQuantity')}
                id="project-estQuantity"
                name="estQuantity"
                data-cy="estQuantity"
                type="text"
              />
              <ValidatedField
                label={translate('productCatalogApp.project.contractStartPeriod')}
                id="project-contractStartPeriod"
                name="contractStartPeriod"
                data-cy="contractStartPeriod"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('productCatalogApp.project.contractEndPeriod')}
                id="project-contractEndPeriod"
                name="contractEndPeriod"
                data-cy="contractEndPeriod"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/project" replace color="info">
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

export default ProjectUpdate;
