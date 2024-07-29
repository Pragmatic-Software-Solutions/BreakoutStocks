import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStockDetails } from '../stock-details.model';
import { StockDetailsService } from '../service/stock-details.service';

const stockDetailsResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockDetails> => {
  const id = route.params['id'];
  if (id) {
    return inject(StockDetailsService)
      .find(id)
      .pipe(
        mergeMap((stockDetails: HttpResponse<IStockDetails>) => {
          if (stockDetails.body) {
            return of(stockDetails.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default stockDetailsResolve;
