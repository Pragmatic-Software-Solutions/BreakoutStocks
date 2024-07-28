import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStockPosition } from '../stock-position.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../stock-position.test-samples';

import { StockPositionService } from './stock-position.service';

const requireRestSample: IStockPosition = {
  ...sampleWithRequiredData,
};

describe('StockPosition Service', () => {
  let service: StockPositionService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockPosition | IStockPosition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StockPositionService);
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

    it('should create a StockPosition', () => {
      const stockPosition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockPosition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockPosition', () => {
      const stockPosition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockPosition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockPosition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockPosition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockPosition', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockPositionToCollectionIfMissing', () => {
      it('should add a StockPosition to an empty array', () => {
        const stockPosition: IStockPosition = sampleWithRequiredData;
        expectedResult = service.addStockPositionToCollectionIfMissing([], stockPosition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockPosition);
      });

      it('should not add a StockPosition to an array that contains it', () => {
        const stockPosition: IStockPosition = sampleWithRequiredData;
        const stockPositionCollection: IStockPosition[] = [
          {
            ...stockPosition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockPositionToCollectionIfMissing(stockPositionCollection, stockPosition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockPosition to an array that doesn't contain it", () => {
        const stockPosition: IStockPosition = sampleWithRequiredData;
        const stockPositionCollection: IStockPosition[] = [sampleWithPartialData];
        expectedResult = service.addStockPositionToCollectionIfMissing(stockPositionCollection, stockPosition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockPosition);
      });

      it('should add only unique StockPosition to an array', () => {
        const stockPositionArray: IStockPosition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockPositionCollection: IStockPosition[] = [sampleWithRequiredData];
        expectedResult = service.addStockPositionToCollectionIfMissing(stockPositionCollection, ...stockPositionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockPosition: IStockPosition = sampleWithRequiredData;
        const stockPosition2: IStockPosition = sampleWithPartialData;
        expectedResult = service.addStockPositionToCollectionIfMissing([], stockPosition, stockPosition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockPosition);
        expect(expectedResult).toContain(stockPosition2);
      });

      it('should accept null and undefined values', () => {
        const stockPosition: IStockPosition = sampleWithRequiredData;
        expectedResult = service.addStockPositionToCollectionIfMissing([], null, stockPosition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockPosition);
      });

      it('should return initial array if no StockPosition is added', () => {
        const stockPositionCollection: IStockPosition[] = [sampleWithRequiredData];
        expectedResult = service.addStockPositionToCollectionIfMissing(stockPositionCollection, undefined, null);
        expect(expectedResult).toEqual(stockPositionCollection);
      });
    });

    describe('compareStockPosition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockPosition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStockPosition(entity1, entity2);
        const compareResult2 = service.compareStockPosition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStockPosition(entity1, entity2);
        const compareResult2 = service.compareStockPosition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStockPosition(entity1, entity2);
        const compareResult2 = service.compareStockPosition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
