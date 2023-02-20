import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBundle } from 'app/shared/model/bundle.model';
import { getEntities } from './bundle.reducer';

export const Bundle = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const bundleList = useAppSelector(state => state.productcatalog.bundle.entities);
  const loading = useAppSelector(state => state.productcatalog.bundle.loading);
  const totalItems = useAppSelector(state => state.productcatalog.bundle.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="bundle-heading" data-cy="BundleHeading">
        <Translate contentKey="productCatalogApp.bundle.home.title">Bundles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="productCatalogApp.bundle.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/bundle/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="productCatalogApp.bundle.home.createLabel">Create new Bundle</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bundleList && bundleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="productCatalogApp.bundle.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('bundleId')}>
                  <Translate contentKey="productCatalogApp.bundle.bundleId">Bundle Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('bundleName')}>
                  <Translate contentKey="productCatalogApp.bundle.bundleName">Bundle Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('quoteTemplateId')}>
                  <Translate contentKey="productCatalogApp.bundle.quoteTemplateId">Quote Template Id</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isCompatible')}>
                  <Translate contentKey="productCatalogApp.bundle.isCompatible">Is Compatible</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('recurringAmount')}>
                  <Translate contentKey="productCatalogApp.bundle.recurringAmount">Recurring Amount</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('singleAmount')}>
                  <Translate contentKey="productCatalogApp.bundle.singleAmount">Single Amount</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('usageAmount')}>
                  <Translate contentKey="productCatalogApp.bundle.usageAmount">Usage Amount</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('totalAmount')}>
                  <Translate contentKey="productCatalogApp.bundle.totalAmount">Total Amount</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="productCatalogApp.bundle.createdAt">Created At</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="productCatalogApp.bundle.createdBy">Created By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  <Translate contentKey="productCatalogApp.bundle.quantity">Quantity</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="productCatalogApp.bundle.cpqQuotedetails">Cpq Quotedetails</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bundleList.map((bundle, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bundle/${bundle.id}`} color="link" size="sm">
                      {bundle.id}
                    </Button>
                  </td>
                  <td>{bundle.bundleId}</td>
                  <td>{bundle.bundleName}</td>
                  <td>{bundle.quoteTemplateId}</td>
                  <td>{bundle.isCompatible ? 'true' : 'false'}</td>
                  <td>{bundle.recurringAmount}</td>
                  <td>{bundle.singleAmount}</td>
                  <td>{bundle.usageAmount}</td>
                  <td>{bundle.totalAmount}</td>
                  <td>{bundle.createdAt ? <TextFormat type="date" value={bundle.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{bundle.createdBy}</td>
                  <td>{bundle.quantity}</td>
                  <td>
                    {bundle.cpqQuotedetails ? (
                      <Link to={`/cpq-quotedetails/${bundle.cpqQuotedetails.id}`}>{bundle.cpqQuotedetails.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bundle/${bundle.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bundle/${bundle.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bundle/${bundle.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="productCatalogApp.bundle.home.notFound">No Bundles found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={bundleList && bundleList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Bundle;
