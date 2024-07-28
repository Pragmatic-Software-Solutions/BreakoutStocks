import { IStock } from 'app/entities/stock/stock.model';

export interface IStockRecommendation {
  id: number;
  entry?: number | null;
  stopLoss?: number | null;
  target?: number | null;
  quantity?: number | null;
  comments?: string | null;
  stock?: Pick<IStock, 'id'> | null;
}

export type NewStockRecommendation = Omit<IStockRecommendation, 'id'> & { id: null };
