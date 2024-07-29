import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../stock-position.test-samples';

import { StockPositionFormService } from './stock-position-form.service';

describe('StockPosition Form Service', () => {
  let service: StockPositionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockPositionFormService);
  });

  describe('Service methods', () => {
    describe('createStockPositionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockPositionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            buyingPrice: expect.any(Object),
            exitPrice: expect.any(Object),
            sold: expect.any(Object),
            quantity: expect.any(Object),
            comments: expect.any(Object),
            stock: expect.any(Object),
          }),
        );
      });

      it('passing IStockPosition should create a new form with FormGroup', () => {
        const formGroup = service.createStockPositionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            buyingPrice: expect.any(Object),
            exitPrice: expect.any(Object),
            sold: expect.any(Object),
            quantity: expect.any(Object),
            comments: expect.any(Object),
            stock: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockPosition', () => {
      it('should return NewStockPosition for default StockPosition initial value', () => {
        const formGroup = service.createStockPositionFormGroup(sampleWithNewData);

        const stockPosition = service.getStockPosition(formGroup) as any;

        expect(stockPosition).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockPosition for empty StockPosition initial value', () => {
        const formGroup = service.createStockPositionFormGroup();

        const stockPosition = service.getStockPosition(formGroup) as any;

        expect(stockPosition).toMatchObject({});
      });

      it('should return IStockPosition', () => {
        const formGroup = service.createStockPositionFormGroup(sampleWithRequiredData);

        const stockPosition = service.getStockPosition(formGroup) as any;

        expect(stockPosition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockPosition should not enable id FormControl', () => {
        const formGroup = service.createStockPositionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockPosition should disable id FormControl', () => {
        const formGroup = service.createStockPositionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
