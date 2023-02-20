import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/bundle">
        <Translate contentKey="global.menu.entities.bundle" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cpq-quotedetails">
        <Translate contentKey="global.menu.entities.cpqQuotedetails" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transaction">
        <Translate contentKey="global.menu.entities.transaction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/version">
        <Translate contentKey="global.menu.entities.version" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/project">
        <Translate contentKey="global.menu.entities.project" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
