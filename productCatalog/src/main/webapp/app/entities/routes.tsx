import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Bundle from './bundle';
import CpqQuotedetails from './cpq-quotedetails';
import Transaction from './transaction';
import Version from './version';
import Project from './project';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('productcatalog', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="bundle/*" element={<Bundle />} />
        <Route path="cpq-quotedetails/*" element={<CpqQuotedetails />} />
        <Route path="transaction/*" element={<Transaction />} />
        <Route path="version/*" element={<Version />} />
        <Route path="project/*" element={<Project />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
