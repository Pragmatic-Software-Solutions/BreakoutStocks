import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStockDetails, NewStockDetails } from '../stock-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockDetails for edit and NewStockDetailsFormGroupInput for create.
 */
type StockDetailsFormGroupInput = IStockDetails | PartialWithRequiredKeyOf<NewStockDetails>;

type StockDetailsFormDefaults = Pick<NewStockDetails, 'id'>;

type StockDetailsFormGroupContent = {
  id: FormControl<IStockDetails['id'] | NewStockDetails['id']>;
  curPrice: FormControl<IStockDetails['curPrice']>;
  priceChange: FormControl<IStockDetails['priceChange']>;
  changePer: FormControl<IStockDetails['changePer']>;
  stock: FormControl<IStockDetails['stock']>;
};

export type StockDetailsFormGroup = FormGroup<StockDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockDetailsFormService {
  createStockDetailsFormGroup(stockDetails: StockDetailsFormGroupInput = { id: null }): StockDetailsFormGroup {
    const stockDetailsRawValue = {
      ...this.getFormDefaults(),
      ...stockDetails,
    };
    return new FormGroup<StockDetailsFormGroupContent>({
      id: new FormControl(
        { value: stockDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      curPrice: new FormControl(stockDetailsRawValue.curPrice),
      priceChange: new FormControl(stockDetailsRawValue.priceChange),
      changePer: new FormControl(stockDetailsRawValue.changePer),
      stock: new FormControl(stockDetailsRawValue.stock),
    });
  }

  getStockDetails(form: StockDetailsFormGroup): IStockDetails | NewStockDetails {
    return form.getRawValue() as IStockDetails | NewStockDetails;
  }

  resetForm(form: StockDetailsFormGroup, stockDetails: StockDetailsFormGroupInput): void {
    const stockDetailsRawValue = { ...this.getFormDefaults(), ...stockDetails };
    form.reset(
      {
        ...stockDetailsRawValue,
        id: { value: stockDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StockDetailsFormDefaults {
    return {
      id: null,
    };
  }
}
