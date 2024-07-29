import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockRecommendation, NewStockRecommendation } from '../stock-recommendation.model';

export type PartialUpdateStockRecommendation = Partial<IStockRecommendation> & Pick<IStockRecommendation, 'id'>;

export type EntityResponseType = HttpResponse<IStockRecommendation>;
export type EntityArrayResponseType = HttpResponse<IStockRecommendation[]>;

@Injectable({ providedIn: 'root' })
export class StockRecommendationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock-recommendations');

  create(stockRecommendation: NewStockRecommendation): Observable<EntityResponseType> {
    return this.http.post<IStockRecommendation>(this.resourceUrl, stockRecommendation, { observe: 'response' });
  }

  update(stockRecommendation: IStockRecommendation): Observable<EntityResponseType> {
    return this.http.put<IStockRecommendation>(
      `${this.resourceUrl}/${this.getStockRecommendationIdentifier(stockRecommendation)}`,
      stockRecommendation,
      { observe: 'response' },
    );
  }

  partialUpdate(stockRecommendation: PartialUpdateStockRecommendation): Observable<EntityResponseType> {
    return this.http.patch<IStockRecommendation>(
      `${this.resourceUrl}/${this.getStockRecommendationIdentifier(stockRecommendation)}`,
      stockRecommendation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStockRecommendation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStockRecommendation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockRecommendationIdentifier(stockRecommendation: Pick<IStockRecommendation, 'id'>): number {
    return stockRecommendation.id;
  }

  compareStockRecommendation(o1: Pick<IStockRecommendation, 'id'> | null, o2: Pick<IStockRecommendation, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockRecommendationIdentifier(o1) === this.getStockRecommendationIdentifier(o2) : o1 === o2;
  }

  addStockRecommendationToCollectionIfMissing<Type extends Pick<IStockRecommendation, 'id'>>(
    stockRecommendationCollection: Type[],
    ...stockRecommendationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockRecommendations: Type[] = stockRecommendationsToCheck.filter(isPresent);
    if (stockRecommendations.length > 0) {
      const stockRecommendationCollectionIdentifiers = stockRecommendationCollection.map(stockRecommendationItem =>
        this.getStockRecommendationIdentifier(stockRecommendationItem),
      );
      const stockRecommendationsToAdd = stockRecommendations.filter(stockRecommendationItem => {
        const stockRecommendationIdentifier = this.getStockRecommendationIdentifier(stockRecommendationItem);
        if (stockRecommendationCollectionIdentifiers.includes(stockRecommendationIdentifier)) {
          return false;
        }
        stockRecommendationCollectionIdentifiers.push(stockRecommendationIdentifier);
        return true;
      });
      return [...stockRecommendationsToAdd, ...stockRecommendationCollection];
    }
    return stockRecommendationCollection;
  }
}
