import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockPosition, NewStockPosition } from '../stock-position.model';

export type PartialUpdateStockPosition = Partial<IStockPosition> & Pick<IStockPosition, 'id'>;

export type EntityResponseType = HttpResponse<IStockPosition>;
export type EntityArrayResponseType = HttpResponse<IStockPosition[]>;

@Injectable({ providedIn: 'root' })
export class StockPositionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock-positions');

  create(stockPosition: NewStockPosition): Observable<EntityResponseType> {
    return this.http.post<IStockPosition>(this.resourceUrl, stockPosition, { observe: 'response' });
  }

  update(stockPosition: IStockPosition): Observable<EntityResponseType> {
    return this.http.put<IStockPosition>(`${this.resourceUrl}/${this.getStockPositionIdentifier(stockPosition)}`, stockPosition, {
      observe: 'response',
    });
  }

  partialUpdate(stockPosition: PartialUpdateStockPosition): Observable<EntityResponseType> {
    return this.http.patch<IStockPosition>(`${this.resourceUrl}/${this.getStockPositionIdentifier(stockPosition)}`, stockPosition, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStockPosition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStockPosition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockPositionIdentifier(stockPosition: Pick<IStockPosition, 'id'>): number {
    return stockPosition.id;
  }

  compareStockPosition(o1: Pick<IStockPosition, 'id'> | null, o2: Pick<IStockPosition, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockPositionIdentifier(o1) === this.getStockPositionIdentifier(o2) : o1 === o2;
  }

  addStockPositionToCollectionIfMissing<Type extends Pick<IStockPosition, 'id'>>(
    stockPositionCollection: Type[],
    ...stockPositionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockPositions: Type[] = stockPositionsToCheck.filter(isPresent);
    if (stockPositions.length > 0) {
      const stockPositionCollectionIdentifiers = stockPositionCollection.map(stockPositionItem =>
        this.getStockPositionIdentifier(stockPositionItem),
      );
      const stockPositionsToAdd = stockPositions.filter(stockPositionItem => {
        const stockPositionIdentifier = this.getStockPositionIdentifier(stockPositionItem);
        if (stockPositionCollectionIdentifiers.includes(stockPositionIdentifier)) {
          return false;
        }
        stockPositionCollectionIdentifiers.push(stockPositionIdentifier);
        return true;
      });
      return [...stockPositionsToAdd, ...stockPositionCollection];
    }
    return stockPositionCollection;
  }
}
