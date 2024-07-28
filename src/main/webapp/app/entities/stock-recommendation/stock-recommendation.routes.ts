import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StockRecommendationComponent } from './list/stock-recommendation.component';
import { StockRecommendationDetailComponent } from './detail/stock-recommendation-detail.component';
import { StockRecommendationUpdateComponent } from './update/stock-recommendation-update.component';
import StockRecommendationResolve from './route/stock-recommendation-routing-resolve.service';

const stockRecommendationRoute: Routes = [
  {
    path: '',
    component: StockRecommendationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StockRecommendationDetailComponent,
    resolve: {
      stockRecommendation: StockRecommendationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StockRecommendationUpdateComponent,
    resolve: {
      stockRecommendation: StockRecommendationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StockRecommendationUpdateComponent,
    resolve: {
      stockRecommendation: StockRecommendationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stockRecommendationRoute;
