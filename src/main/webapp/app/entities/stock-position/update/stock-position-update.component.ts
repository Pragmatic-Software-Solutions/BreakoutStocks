import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { IStockPosition } from '../stock-position.model';
import { StockPositionService } from '../service/stock-position.service';
import { StockPositionFormService, StockPositionFormGroup } from './stock-position-form.service';

@Component({
  standalone: true,
  selector: 'jhi-stock-position-update',
  templateUrl: './stock-position-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockPositionUpdateComponent implements OnInit {
  isSaving = false;
  stockPosition: IStockPosition | null = null;

  stocksSharedCollection: IStock[] = [];

  protected stockPositionService = inject(StockPositionService);
  protected stockPositionFormService = inject(StockPositionFormService);
  protected stockService = inject(StockService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockPositionFormGroup = this.stockPositionFormService.createStockPositionFormGroup();

  compareStock = (o1: IStock | null, o2: IStock | null): boolean => this.stockService.compareStock(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockPosition }) => {
      this.stockPosition = stockPosition;
      if (stockPosition) {
        this.updateForm(stockPosition);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockPosition = this.stockPositionFormService.getStockPosition(this.editForm);
    if (stockPosition.id !== null) {
      this.subscribeToSaveResponse(this.stockPositionService.update(stockPosition));
    } else {
      this.subscribeToSaveResponse(this.stockPositionService.create(stockPosition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockPosition>>): void {
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

  protected updateForm(stockPosition: IStockPosition): void {
    this.stockPosition = stockPosition;
    this.stockPositionFormService.resetForm(this.editForm, stockPosition);

    this.stocksSharedCollection = this.stockService.addStockToCollectionIfMissing<IStock>(this.stocksSharedCollection, stockPosition.stock);
  }

  protected loadRelationshipsOptions(): void {
    this.stockService
      .query()
      .pipe(map((res: HttpResponse<IStock[]>) => res.body ?? []))
      .pipe(map((stocks: IStock[]) => this.stockService.addStockToCollectionIfMissing<IStock>(stocks, this.stockPosition?.stock)))
      .subscribe((stocks: IStock[]) => (this.stocksSharedCollection = stocks));
  }
}
