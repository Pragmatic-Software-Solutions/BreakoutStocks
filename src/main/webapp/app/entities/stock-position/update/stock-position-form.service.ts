import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStockPosition, NewStockPosition } from '../stock-position.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockPosition for edit and NewStockPositionFormGroupInput for create.
 */
type StockPositionFormGroupInput = IStockPosition | PartialWithRequiredKeyOf<NewStockPosition>;

type StockPositionFormDefaults = Pick<NewStockPosition, 'id' | 'sold'>;

type StockPositionFormGroupContent = {
  id: FormControl<IStockPosition['id'] | NewStockPosition['id']>;
  buyingPrice: FormControl<IStockPosition['buyingPrice']>;
  exitPrice: FormControl<IStockPosition['exitPrice']>;
  sold: FormControl<IStockPosition['sold']>;
  quantity: FormControl<IStockPosition['quantity']>;
  comments: FormControl<IStockPosition['comments']>;
  stock: FormControl<IStockPosition['stock']>;
};

export type StockPositionFormGroup = FormGroup<StockPositionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockPositionFormService {
  createStockPositionFormGroup(stockPosition: StockPositionFormGroupInput = { id: null }): StockPositionFormGroup {
    const stockPositionRawValue = {
      ...this.getFormDefaults(),
      ...stockPosition,
    };
    return new FormGroup<StockPositionFormGroupContent>({
      id: new FormControl(
        { value: stockPositionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      buyingPrice: new FormControl(stockPositionRawValue.buyingPrice),
      exitPrice: new FormControl(stockPositionRawValue.exitPrice),
      sold: new FormControl(stockPositionRawValue.sold),
      quantity: new FormControl(stockPositionRawValue.quantity),
      comments: new FormControl(stockPositionRawValue.comments),
      stock: new FormControl(stockPositionRawValue.stock),
    });
  }

  getStockPosition(form: StockPositionFormGroup): IStockPosition | NewStockPosition {
    return form.getRawValue() as IStockPosition | NewStockPosition;
  }

  resetForm(form: StockPositionFormGroup, stockPosition: StockPositionFormGroupInput): void {
    const stockPositionRawValue = { ...this.getFormDefaults(), ...stockPosition };
    form.reset(
      {
        ...stockPositionRawValue,
        id: { value: stockPositionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StockPositionFormDefaults {
    return {
      id: null,
      sold: false,
    };
  }
}
