import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockDetails, NewStockDetails } from '../stock-details.model';

export type PartialUpdateStockDetails = Partial<IStockDetails> & Pick<IStockDetails, 'id'>;

export type EntityResponseType = HttpResponse<IStockDetails>;
export type EntityArrayResponseType = HttpResponse<IStockDetails[]>;

@Injectable({ providedIn: 'root' })
export class StockDetailsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock-details');

  create(stockDetails: NewStockDetails): Observable<EntityResponseType> {
    return this.http.post<IStockDetails>(this.resourceUrl, stockDetails, { observe: 'response' });
  }

  update(stockDetails: IStockDetails): Observable<EntityResponseType> {
    return this.http.put<IStockDetails>(`${this.resourceUrl}/${this.getStockDetailsIdentifier(stockDetails)}`, stockDetails, {
      observe: 'response',
    });
  }

  partialUpdate(stockDetails: PartialUpdateStockDetails): Observable<EntityResponseType> {
    return this.http.patch<IStockDetails>(`${this.resourceUrl}/${this.getStockDetailsIdentifier(stockDetails)}`, stockDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStockDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStockDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockDetailsIdentifier(stockDetails: Pick<IStockDetails, 'id'>): number {
    return stockDetails.id;
  }

  compareStockDetails(o1: Pick<IStockDetails, 'id'> | null, o2: Pick<IStockDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockDetailsIdentifier(o1) === this.getStockDetailsIdentifier(o2) : o1 === o2;
  }

  addStockDetailsToCollectionIfMissing<Type extends Pick<IStockDetails, 'id'>>(
    stockDetailsCollection: Type[],
    ...stockDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockDetails: Type[] = stockDetailsToCheck.filter(isPresent);
    if (stockDetails.length > 0) {
      const stockDetailsCollectionIdentifiers = stockDetailsCollection.map(stockDetailsItem =>
        this.getStockDetailsIdentifier(stockDetailsItem),
      );
      const stockDetailsToAdd = stockDetails.filter(stockDetailsItem => {
        const stockDetailsIdentifier = this.getStockDetailsIdentifier(stockDetailsItem);
        if (stockDetailsCollectionIdentifiers.includes(stockDetailsIdentifier)) {
          return false;
        }
        stockDetailsCollectionIdentifiers.push(stockDetailsIdentifier);
        return true;
      });
      return [...stockDetailsToAdd, ...stockDetailsCollection];
    }
    return stockDetailsCollection;
  }
}
