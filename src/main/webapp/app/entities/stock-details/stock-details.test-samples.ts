import { IStockDetails, NewStockDetails } from './stock-details.model';

export const sampleWithRequiredData: IStockDetails = {
  id: 27332,
};

export const sampleWithPartialData: IStockDetails = {
  id: 23215,
  changePer: 25228.68,
};

export const sampleWithFullData: IStockDetails = {
  id: 18311,
  curPrice: 15237.17,
  priceChange: 11454.2,
  changePer: 31827.67,
};

export const sampleWithNewData: NewStockDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
