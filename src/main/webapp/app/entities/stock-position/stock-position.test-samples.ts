import { IStockPosition, NewStockPosition } from './stock-position.model';

export const sampleWithRequiredData: IStockPosition = {
  id: 22487,
};

export const sampleWithPartialData: IStockPosition = {
  id: 24727,
  buyingPrice: 18437.15,
  exitPrice: 16463.27,
  sold: false,
  comments: 'complex mug',
};

export const sampleWithFullData: IStockPosition = {
  id: 10496,
  buyingPrice: 25244.24,
  exitPrice: 21687.39,
  sold: true,
  quantity: 28013,
  comments: 'save till optimal',
};

export const sampleWithNewData: NewStockPosition = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
