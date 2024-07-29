import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'breakoutStocksApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'stock',
    data: { pageTitle: 'breakoutStocksApp.stock.home.title' },
    loadChildren: () => import('./stock/stock.routes'),
  },
  {
    path: 'stock-details',
    data: { pageTitle: 'breakoutStocksApp.stockDetails.home.title' },
    loadChildren: () => import('./stock-details/stock-details.routes'),
  },
  {
    path: 'stock-recommendation',
    data: { pageTitle: 'breakoutStocksApp.stockRecommendation.home.title' },
    loadChildren: () => import('./stock-recommendation/stock-recommendation.routes'),
  },
  {
    path: 'stock-position',
    data: { pageTitle: 'breakoutStocksApp.stockPosition.home.title' },
    loadChildren: () => import('./stock-position/stock-position.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
