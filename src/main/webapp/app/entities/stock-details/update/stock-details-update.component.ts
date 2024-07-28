import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { IStockDetails } from '../stock-details.model';
import { StockDetailsService } from '../service/stock-details.service';
import { StockDetailsFormService, StockDetailsFormGroup } from './stock-details-form.service';

@Component({
  standalone: true,
  selector: 'jhi-stock-details-update',
  templateUrl: './stock-details-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockDetailsUpdateComponent implements OnInit {
  isSaving = false;
  stockDetails: IStockDetails | null = null;

  stocksCollection: IStock[] = [];

  protected stockDetailsService = inject(StockDetailsService);
  protected stockDetailsFormService = inject(StockDetailsFormService);
  protected stockService = inject(StockService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockDetailsFormGroup = this.stockDetailsFormService.createStockDetailsFormGroup();

  compareStock = (o1: IStock | null, o2: IStock | null): boolean => this.stockService.compareStock(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockDetails }) => {
      this.stockDetails = stockDetails;
      if (stockDetails) {
        this.updateForm(stockDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockDetails = this.stockDetailsFormService.getStockDetails(this.editForm);
    if (stockDetails.id !== null) {
      this.subscribeToSaveResponse(this.stockDetailsService.update(stockDetails));
    } else {
      this.subscribeToSaveResponse(this.stockDetailsService.create(stockDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockDetails>>): void {
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

  protected updateForm(stockDetails: IStockDetails): void {
    this.stockDetails = stockDetails;
    this.stockDetailsFormService.resetForm(this.editForm, stockDetails);

    this.stocksCollection = this.stockService.addStockToCollectionIfMissing<IStock>(this.stocksCollection, stockDetails.stock);
  }

  protected loadRelationshipsOptions(): void {
    this.stockService
      .query({ filter: 'stockdetails-is-null' })
      .pipe(map((res: HttpResponse<IStock[]>) => res.body ?? []))
      .pipe(map((stocks: IStock[]) => this.stockService.addStockToCollectionIfMissing<IStock>(stocks, this.stockDetails?.stock)))
      .subscribe((stocks: IStock[]) => (this.stocksCollection = stocks));
  }
}
