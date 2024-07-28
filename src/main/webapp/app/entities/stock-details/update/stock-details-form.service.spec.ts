import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../stock-details.test-samples';

import { StockDetailsFormService } from './stock-details-form.service';

describe('StockDetails Form Service', () => {
  let service: StockDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createStockDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            curPrice: expect.any(Object),
            priceChange: expect.any(Object),
            changePer: expect.any(Object),
            stock: expect.any(Object),
          }),
        );
      });

      it('passing IStockDetails should create a new form with FormGroup', () => {
        const formGroup = service.createStockDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            curPrice: expect.any(Object),
            priceChange: expect.any(Object),
            changePer: expect.any(Object),
            stock: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockDetails', () => {
      it('should return NewStockDetails for default StockDetails initial value', () => {
        const formGroup = service.createStockDetailsFormGroup(sampleWithNewData);

        const stockDetails = service.getStockDetails(formGroup) as any;

        expect(stockDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockDetails for empty StockDetails initial value', () => {
        const formGroup = service.createStockDetailsFormGroup();

        const stockDetails = service.getStockDetails(formGroup) as any;

        expect(stockDetails).toMatchObject({});
      });

      it('should return IStockDetails', () => {
        const formGroup = service.createStockDetailsFormGroup(sampleWithRequiredData);

        const stockDetails = service.getStockDetails(formGroup) as any;

        expect(stockDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockDetails should not enable id FormControl', () => {
        const formGroup = service.createStockDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockDetails should disable id FormControl', () => {
        const formGroup = service.createStockDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
