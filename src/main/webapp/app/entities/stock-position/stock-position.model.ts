import { IStock } from 'app/entities/stock/stock.model';

export interface IStockPosition {
  id: number;
  buyingPrice?: number | null;
  exitPrice?: number | null;
  sold?: boolean | null;
  quantity?: number | null;
  comments?: string | null;
  stock?: Pick<IStock, 'id'> | null;
}

export type NewStockPosition = Omit<IStockPosition, 'id'> & { id: null };
