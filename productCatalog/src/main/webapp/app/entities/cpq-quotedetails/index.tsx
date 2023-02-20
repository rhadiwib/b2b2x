import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CpqQuotedetails from './cpq-quotedetails';
import CpqQuotedetailsDetail from './cpq-quotedetails-detail';
import CpqQuotedetailsUpdate from './cpq-quotedetails-update';
import CpqQuotedetailsDeleteDialog from './cpq-quotedetails-delete-dialog';

const CpqQuotedetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CpqQuotedetails />} />
    <Route path="new" element={<CpqQuotedetailsUpdate />} />
    <Route path=":id">
      <Route index element={<CpqQuotedetailsDetail />} />
      <Route path="edit" element={<CpqQuotedetailsUpdate />} />
      <Route path="delete" element={<CpqQuotedetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CpqQuotedetailsRoutes;
