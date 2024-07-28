import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IStockRecommendation } from '../stock-recommendation.model';

@Component({
  standalone: true,
  selector: 'jhi-stock-recommendation-detail',
  templateUrl: './stock-recommendation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StockRecommendationDetailComponent {
  stockRecommendation = input<IStockRecommendation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
