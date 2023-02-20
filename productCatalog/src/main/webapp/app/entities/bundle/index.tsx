import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Bundle from './bundle';
import BundleDetail from './bundle-detail';
import BundleUpdate from './bundle-update';
import BundleDeleteDialog from './bundle-delete-dialog';

const BundleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Bundle />} />
    <Route path="new" element={<BundleUpdate />} />
    <Route path=":id">
      <Route index element={<BundleDetail />} />
      <Route path="edit" element={<BundleUpdate />} />
      <Route path="delete" element={<BundleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BundleRoutes;
