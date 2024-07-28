import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IStockPosition } from '../stock-position.model';

@Component({
  standalone: true,
  selector: 'jhi-stock-position-detail',
  templateUrl: './stock-position-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StockPositionDetailComponent {
  stockPosition = input<IStockPosition | null>(null);

  previousState(): void {
    window.history.back();
  }
}
