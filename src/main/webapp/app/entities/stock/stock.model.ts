export interface IStock {
  id: number;
  symbol?: string | null;
  exchange?: string | null;
}

export type NewStock = Omit<IStock, 'id'> & { id: null };
