import { IStock } from 'app/entities/stock/stock.model';

export interface IStockDetails {
  id: number;
  curPrice?: number | null;
  priceChange?: number | null;
  changePer?: number | null;
  stock?: Pick<IStock, 'id'> | null;
}

export type NewStockDetails = Omit<IStockDetails, 'id'> & { id: null };
