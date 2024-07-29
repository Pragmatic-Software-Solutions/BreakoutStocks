import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStockRecommendation } from '../stock-recommendation.model';
import { StockRecommendationService } from '../service/stock-recommendation.service';

const stockRecommendationResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockRecommendation> => {
  const id = route.params['id'];
  if (id) {
    return inject(StockRecommendationService)
      .find(id)
      .pipe(
        mergeMap((stockRecommendation: HttpResponse<IStockRecommendation>) => {
          if (stockRecommendation.body) {
            return of(stockRecommendation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default stockRecommendationResolve;
