import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStockRecommendation } from '../stock-recommendation.model';
import { StockRecommendationService } from '../service/stock-recommendation.service';

@Component({
  standalone: true,
  templateUrl: './stock-recommendation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StockRecommendationDeleteDialogComponent {
  stockRecommendation?: IStockRecommendation;

  protected stockRecommendationService = inject(StockRecommendationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockRecommendationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
