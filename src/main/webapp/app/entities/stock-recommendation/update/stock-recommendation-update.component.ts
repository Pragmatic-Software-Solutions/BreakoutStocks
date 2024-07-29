import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { IStockRecommendation } from '../stock-recommendation.model';
import { StockRecommendationService } from '../service/stock-recommendation.service';
import { StockRecommendationFormService, StockRecommendationFormGroup } from './stock-recommendation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-stock-recommendation-update',
  templateUrl: './stock-recommendation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockRecommendationUpdateComponent implements OnInit {
  isSaving = false;
  stockRecommendation: IStockRecommendation | null = null;

  stocksSharedCollection: IStock[] = [];

  protected stockRecommendationService = inject(StockRecommendationService);
  protected stockRecommendationFormService = inject(StockRecommendationFormService);
  protected stockService = inject(StockService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockRecommendationFormGroup = this.stockRecommendationFormService.createStockRecommendationFormGroup();

  compareStock = (o1: IStock | null, o2: IStock | null): boolean => this.stockService.compareStock(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockRecommendation }) => {
      this.stockRecommendation = stockRecommendation;
      if (stockRecommendation) {
        this.updateForm(stockRecommendation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockRecommendation = this.stockRecommendationFormService.getStockRecommendation(this.editForm);
    if (stockRecommendation.id !== null) {
      this.subscribeToSaveResponse(this.stockRecommendationService.update(stockRecommendation));
    } else {
      this.subscribeToSaveResponse(this.stockRecommendationService.create(stockRecommendation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockRecommendation>>): void {
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

  protected updateForm(stockRecommendation: IStockRecommendation): void {
    this.stockRecommendation = stockRecommendation;
    this.stockRecommendationFormService.resetForm(this.editForm, stockRecommendation);

    this.stocksSharedCollection = this.stockService.addStockToCollectionIfMissing<IStock>(
      this.stocksSharedCollection,
      stockRecommendation.stock,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stockService
      .query()
      .pipe(map((res: HttpResponse<IStock[]>) => res.body ?? []))
      .pipe(map((stocks: IStock[]) => this.stockService.addStockToCollectionIfMissing<IStock>(stocks, this.stockRecommendation?.stock)))
      .subscribe((stocks: IStock[]) => (this.stocksSharedCollection = stocks));
  }
}
