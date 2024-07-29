import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IStockRecommendation } from '../stock-recommendation.model';
import { StockRecommendationService } from '../service/stock-recommendation.service';

import stockRecommendationResolve from './stock-recommendation-routing-resolve.service';

describe('StockRecommendation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: StockRecommendationService;
  let resultStockRecommendation: IStockRecommendation | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(StockRecommendationService);
    resultStockRecommendation = undefined;
  });

  describe('resolve', () => {
    it('should return IStockRecommendation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        stockRecommendationResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStockRecommendation = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultStockRecommendation).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        stockRecommendationResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStockRecommendation = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStockRecommendation).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IStockRecommendation>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        stockRecommendationResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStockRecommendation = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultStockRecommendation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
