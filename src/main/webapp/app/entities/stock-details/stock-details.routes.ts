import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StockDetailsComponent } from './list/stock-details.component';
import { StockDetailsDetailComponent } from './detail/stock-details-detail.component';
import { StockDetailsUpdateComponent } from './update/stock-details-update.component';
import StockDetailsResolve from './route/stock-details-routing-resolve.service';

const stockDetailsRoute: Routes = [
  {
    path: '',
    component: StockDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StockDetailsDetailComponent,
    resolve: {
      stockDetails: StockDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StockDetailsUpdateComponent,
    resolve: {
      stockDetails: StockDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StockDetailsUpdateComponent,
    resolve: {
      stockDetails: StockDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stockDetailsRoute;
