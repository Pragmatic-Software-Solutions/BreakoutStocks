import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStockDetails } from '../stock-details.model';
import { StockDetailsService } from '../service/stock-details.service';

@Component({
  standalone: true,
  templateUrl: './stock-details-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StockDetailsDeleteDialogComponent {
  stockDetails?: IStockDetails;

  protected stockDetailsService = inject(StockDetailsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
