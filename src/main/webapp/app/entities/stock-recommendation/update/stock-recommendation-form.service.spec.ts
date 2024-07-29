import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../stock-recommendation.test-samples';

import { StockRecommendationFormService } from './stock-recommendation-form.service';

describe('StockRecommendation Form Service', () => {
  let service: StockRecommendationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockRecommendationFormService);
  });

  describe('Service methods', () => {
    describe('createStockRecommendationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockRecommendationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            entry: expect.any(Object),
            stopLoss: expect.any(Object),
            target: expect.any(Object),
            quantity: expect.any(Object),
            comments: expect.any(Object),
            stock: expect.any(Object),
          }),
        );
      });

      it('passing IStockRecommendation should create a new form with FormGroup', () => {
        const formGroup = service.createStockRecommendationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            entry: expect.any(Object),
            stopLoss: expect.any(Object),
            target: expect.any(Object),
            quantity: expect.any(Object),
            comments: expect.any(Object),
            stock: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockRecommendation', () => {
      it('should return NewStockRecommendation for default StockRecommendation initial value', () => {
        const formGroup = service.createStockRecommendationFormGroup(sampleWithNewData);

        const stockRecommendation = service.getStockRecommendation(formGroup) as any;

        expect(stockRecommendation).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockRecommendation for empty StockRecommendation initial value', () => {
        const formGroup = service.createStockRecommendationFormGroup();

        const stockRecommendation = service.getStockRecommendation(formGroup) as any;

        expect(stockRecommendation).toMatchObject({});
      });

      it('should return IStockRecommendation', () => {
        const formGroup = service.createStockRecommendationFormGroup(sampleWithRequiredData);

        const stockRecommendation = service.getStockRecommendation(formGroup) as any;

        expect(stockRecommendation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockRecommendation should not enable id FormControl', () => {
        const formGroup = service.createStockRecommendationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockRecommendation should disable id FormControl', () => {
        const formGroup = service.createStockRecommendationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
