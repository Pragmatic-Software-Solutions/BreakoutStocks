import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StockPositionComponent } from './list/stock-position.component';
import { StockPositionDetailComponent } from './detail/stock-position-detail.component';
import { StockPositionUpdateComponent } from './update/stock-position-update.component';
import StockPositionResolve from './route/stock-position-routing-resolve.service';

const stockPositionRoute: Routes = [
  {
    path: '',
    component: StockPositionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StockPositionDetailComponent,
    resolve: {
      stockPosition: StockPositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StockPositionUpdateComponent,
    resolve: {
      stockPosition: StockPositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StockPositionUpdateComponent,
    resolve: {
      stockPosition: StockPositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stockPositionRoute;
