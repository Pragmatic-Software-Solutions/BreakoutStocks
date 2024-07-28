import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStock } from '../stock.model';
import { StockService } from '../service/stock.service';
import { StockFormService, StockFormGroup } from './stock-form.service';

@Component({
  standalone: true,
  selector: 'jhi-stock-update',
  templateUrl: './stock-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockUpdateComponent implements OnInit {
  isSaving = false;
  stock: IStock | null = null;

  protected stockService = inject(StockService);
  protected stockFormService = inject(StockFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockFormGroup = this.stockFormService.createStockFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stock }) => {
      this.stock = stock;
      if (stock) {
        this.updateForm(stock);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stock = this.stockFormService.getStock(this.editForm);
    if (stock.id !== null) {
      this.subscribeToSaveResponse(this.stockService.update(stock));
    } else {
      this.subscribeToSaveResponse(this.stockService.create(stock));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(stock: IStock): void {
    this.stock = stock;
    this.stockFormService.resetForm(this.editForm, stock);
  }
}
