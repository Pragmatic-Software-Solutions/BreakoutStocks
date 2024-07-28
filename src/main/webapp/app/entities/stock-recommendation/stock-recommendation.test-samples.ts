import { IStockRecommendation, NewStockRecommendation } from './stock-recommendation.model';

export const sampleWithRequiredData: IStockRecommendation = {
  id: 19105,
};

export const sampleWithPartialData: IStockRecommendation = {
  id: 29282,
};

export const sampleWithFullData: IStockRecommendation = {
  id: 31015,
  entry: 9616.79,
  stopLoss: 14606.01,
  target: 30806.66,
  quantity: 7330,
  comments: 'after anenst',
};

export const sampleWithNewData: NewStockRecommendation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
