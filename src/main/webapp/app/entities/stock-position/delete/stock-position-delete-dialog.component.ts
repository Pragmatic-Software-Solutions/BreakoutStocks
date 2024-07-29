import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStockPosition } from '../stock-position.model';
import { StockPositionService } from '../service/stock-position.service';

@Component({
  standalone: true,
  templateUrl: './stock-position-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StockPositionDeleteDialogComponent {
  stockPosition?: IStockPosition;

  protected stockPositionService = inject(StockPositionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockPositionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
