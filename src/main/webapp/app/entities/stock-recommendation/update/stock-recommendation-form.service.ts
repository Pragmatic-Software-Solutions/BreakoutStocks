import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStockRecommendation, NewStockRecommendation } from '../stock-recommendation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockRecommendation for edit and NewStockRecommendationFormGroupInput for create.
 */
type StockRecommendationFormGroupInput = IStockRecommendation | PartialWithRequiredKeyOf<NewStockRecommendation>;

type StockRecommendationFormDefaults = Pick<NewStockRecommendation, 'id'>;

type StockRecommendationFormGroupContent = {
  id: FormControl<IStockRecommendation['id'] | NewStockRecommendation['id']>;
  entry: FormControl<IStockRecommendation['entry']>;
  stopLoss: FormControl<IStockRecommendation['stopLoss']>;
  target: FormControl<IStockRecommendation['target']>;
  quantity: FormControl<IStockRecommendation['quantity']>;
  comments: FormControl<IStockRecommendation['comments']>;
  stock: FormControl<IStockRecommendation['stock']>;
};

export type StockRecommendationFormGroup = FormGroup<StockRecommendationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockRecommendationFormService {
  createStockRecommendationFormGroup(stockRecommendation: StockRecommendationFormGroupInput = { id: null }): StockRecommendationFormGroup {
    const stockRecommendationRawValue = {
      ...this.getFormDefaults(),
      ...stockRecommendation,
    };
    return new FormGroup<StockRecommendationFormGroupContent>({
      id: new FormControl(
        { value: stockRecommendationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      entry: new FormControl(stockRecommendationRawValue.entry),
      stopLoss: new FormControl(stockRecommendationRawValue.stopLoss),
      target: new FormControl(stockRecommendationRawValue.target),
      quantity: new FormControl(stockRecommendationRawValue.quantity),
      comments: new FormControl(stockRecommendationRawValue.comments),
      stock: new FormControl(stockRecommendationRawValue.stock),
    });
  }

  getStockRecommendation(form: StockRecommendationFormGroup): IStockRecommendation | NewStockRecommendation {
    return form.getRawValue() as IStockRecommendation | NewStockRecommendation;
  }

  resetForm(form: StockRecommendationFormGroup, stockRecommendation: StockRecommendationFormGroupInput): void {
    const stockRecommendationRawValue = { ...this.getFormDefaults(), ...stockRecommendation };
    form.reset(
      {
        ...stockRecommendationRawValue,
        id: { value: stockRecommendationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StockRecommendationFormDefaults {
    return {
      id: null,
    };
  }
}
