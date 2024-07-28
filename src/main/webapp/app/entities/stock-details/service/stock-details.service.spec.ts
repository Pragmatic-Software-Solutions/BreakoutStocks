import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStockDetails } from '../stock-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../stock-details.test-samples';

import { StockDetailsService } from './stock-details.service';

const requireRestSample: IStockDetails = {
  ...sampleWithRequiredData,
};

describe('StockDetails Service', () => {
  let service: StockDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockDetails | IStockDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StockDetailsService);
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

    it('should create a StockDetails', () => {
      const stockDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockDetails', () => {
      const stockDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockDetailsToCollectionIfMissing', () => {
      it('should add a StockDetails to an empty array', () => {
        const stockDetails: IStockDetails = sampleWithRequiredData;
        expectedResult = service.addStockDetailsToCollectionIfMissing([], stockDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockDetails);
      });

      it('should not add a StockDetails to an array that contains it', () => {
        const stockDetails: IStockDetails = sampleWithRequiredData;
        const stockDetailsCollection: IStockDetails[] = [
          {
            ...stockDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockDetailsToCollectionIfMissing(stockDetailsCollection, stockDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockDetails to an array that doesn't contain it", () => {
        const stockDetails: IStockDetails = sampleWithRequiredData;
        const stockDetailsCollection: IStockDetails[] = [sampleWithPartialData];
        expectedResult = service.addStockDetailsToCollectionIfMissing(stockDetailsCollection, stockDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockDetails);
      });

      it('should add only unique StockDetails to an array', () => {
        const stockDetailsArray: IStockDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockDetailsCollection: IStockDetails[] = [sampleWithRequiredData];
        expectedResult = service.addStockDetailsToCollectionIfMissing(stockDetailsCollection, ...stockDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockDetails: IStockDetails = sampleWithRequiredData;
        const stockDetails2: IStockDetails = sampleWithPartialData;
        expectedResult = service.addStockDetailsToCollectionIfMissing([], stockDetails, stockDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockDetails);
        expect(expectedResult).toContain(stockDetails2);
      });

      it('should accept null and undefined values', () => {
        const stockDetails: IStockDetails = sampleWithRequiredData;
        expectedResult = service.addStockDetailsToCollectionIfMissing([], null, stockDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockDetails);
      });

      it('should return initial array if no StockDetails is added', () => {
        const stockDetailsCollection: IStockDetails[] = [sampleWithRequiredData];
        expectedResult = service.addStockDetailsToCollectionIfMissing(stockDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(stockDetailsCollection);
      });
    });

    describe('compareStockDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStockDetails(entity1, entity2);
        const compareResult2 = service.compareStockDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStockDetails(entity1, entity2);
        const compareResult2 = service.compareStockDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStockDetails(entity1, entity2);
        const compareResult2 = service.compareStockDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
