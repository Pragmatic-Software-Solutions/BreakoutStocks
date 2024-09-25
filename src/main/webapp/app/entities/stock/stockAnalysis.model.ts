export interface IStockAnalysis {
  id: number;
  pros?: array | null;
  cons?: array | null;
}

export type NewAnalysis = Omit<IStockAnalysis, 'id'> & { id: null };
