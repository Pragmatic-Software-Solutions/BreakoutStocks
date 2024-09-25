export interface IStock {
  id: number;
  symbol?: string | null;
  exchange?: string | null;
  result?: Pick<IStockAnalysis, 'id' | 'pros' | 'cons'> | null;
}

export type NewStock = Omit<IStock, 'id'> & { id: null };

export interface IStockAnalysis {
  id: number;
  pros?: string[] | null;
  cons?: string[] | null;
}

export type NewAnalysis = Omit<IStockAnalysis, 'id'> & { id: null };
