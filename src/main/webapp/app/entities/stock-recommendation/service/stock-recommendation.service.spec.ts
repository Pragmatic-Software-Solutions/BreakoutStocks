import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStockRecommendation } from '../stock-recommendation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../stock-recommendation.test-samples';

import { StockRecommendationService } from './stock-recommendation.service';

const requireRestSample: IStockRecommendation = {
  ...sampleWithRequiredData,
};

describe('StockRecommendation Service', () => {
  let service: StockRecommendationService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockRecommendation | IStockRecommendation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StockRecommendationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a StockRecommendation', () => {
      const stockRecommendation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockRecommendation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockRecommendation', () => {
      const stockRecommendation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockRecommendation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockRecommendation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockRecommendation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockRecommendation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockRecommendationToCollectionIfMissing', () => {
      it('should add a StockRecommendation to an empty array', () => {
        const stockRecommendation: IStockRecommendation = sampleWithRequiredData;
        expectedResult = service.addStockRecommendationToCollectionIfMissing([], stockRecommendation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockRecommendation);
      });

      it('should not add a StockRecommendation to an array that contains it', () => {
        const stockRecommendation: IStockRecommendation = sampleWithRequiredData;
        const stockRecommendationCollection: IStockRecommendation[] = [
          {
            ...stockRecommendation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockRecommendationToCollectionIfMissing(stockRecommendationCollection, stockRecommendation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockRecommendation to an array that doesn't contain it", () => {
        const stockRecommendation: IStockRecommendation = sampleWithRequiredData;
        const stockRecommendationCollection: IStockRecommendation[] = [sampleWithPartialData];
        expectedResult = service.addStockRecommendationToCollectionIfMissing(stockRecommendationCollection, stockRecommendation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockRecommendation);
      });

      it('should add only unique StockRecommendation to an array', () => {
        const stockRecommendationArray: IStockRecommendation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockRecommendationCollection: IStockRecommendation[] = [sampleWithRequiredData];
        expectedResult = service.addStockRecommendationToCollectionIfMissing(stockRecommendationCollection, ...stockRecommendationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockRecommendation: IStockRecommendation = sampleWithRequiredData;
        const stockRecommendation2: IStockRecommendation = sampleWithPartialData;
        expectedResult = service.addStockRecommendationToCollectionIfMissing([], stockRecommendation, stockRecommendation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockRecommendation);
        expect(expectedResult).toContain(stockRecommendation2);
      });

      it('should accept null and undefined values', () => {
        const stockRecommendation: IStockRecommendation = sampleWithRequiredData;
        expectedResult = service.addStockRecommendationToCollectionIfMissing([], null, stockRecommendation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockRecommendation);
      });

      it('should return initial array if no StockRecommendation is added', () => {
        const stockRecommendationCollection: IStockRecommendation[] = [sampleWithRequiredData];
        expectedResult = service.addStockRecommendationToCollectionIfMissing(stockRecommendationCollection, undefined, null);
        expect(expectedResult).toEqual(stockRecommendationCollection);
      });
    });

    describe('compareStockRecommendation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockRecommendation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStockRecommendation(entity1, entity2);
        const compareResult2 = service.compareStockRecommendation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStockRecommendation(entity1, entity2);
        const compareResult2 = service.compareStockRecommendation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStockRecommendation(entity1, entity2);
        const compareResult2 = service.compareStockRecommendation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
