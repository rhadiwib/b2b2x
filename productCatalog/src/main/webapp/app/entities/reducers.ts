import bundle from 'app/entities/bundle/bundle.reducer';
import cpqQuotedetails from 'app/entities/cpq-quotedetails/cpq-quotedetails.reducer';
import transaction from 'app/entities/transaction/transaction.reducer';
import version from 'app/entities/version/version.reducer';
import project from 'app/entities/project/project.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  bundle,
  cpqQuotedetails,
  transaction,
  version,
  project,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
