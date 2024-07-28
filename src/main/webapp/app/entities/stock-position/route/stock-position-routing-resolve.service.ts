import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStockPosition } from '../stock-position.model';
import { StockPositionService } from '../service/stock-position.service';

const stockPositionResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockPosition> => {
  const id = route.params['id'];
  if (id) {
    return inject(StockPositionService)
      .find(id)
      .pipe(
        mergeMap((stockPosition: HttpResponse<IStockPosition>) => {
          if (stockPosition.body) {
            return of(stockPosition.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default stockPositionResolve;
