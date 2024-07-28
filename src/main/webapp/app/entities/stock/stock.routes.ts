import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StockComponent } from './list/stock.component';
import { StockDetailComponent } from './detail/stock-detail.component';
import { StockUpdateComponent } from './update/stock-update.component';
import StockResolve from './route/stock-routing-resolve.service';

const stockRoute: Routes = [
  {
    path: '',
    component: StockComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StockDetailComponent,
    resolve: {
      stock: StockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StockUpdateComponent,
    resolve: {
      stock: StockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StockUpdateComponent,
    resolve: {
      stock: StockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stockRoute;
