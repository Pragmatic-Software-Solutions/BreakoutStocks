import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IStockDetails } from '../stock-details.model';

@Component({
  standalone: true,
  selector: 'jhi-stock-details-detail',
  templateUrl: './stock-details-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StockDetailsDetailComponent {
  stockDetails = input<IStockDetails | null>(null);

  previousState(): void {
    window.history.back();
  }
}
