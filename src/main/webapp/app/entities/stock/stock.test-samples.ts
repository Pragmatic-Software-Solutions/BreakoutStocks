import { IStock, NewStock } from './stock.model';

export const sampleWithRequiredData: IStock = {
  id: 21432,
};

export const sampleWithPartialData: IStock = {
  id: 27452,
  symbol: 'servitude where',
  exchange: 'titivate',
};

export const sampleWithFullData: IStock = {
  id: 7462,
  symbol: 'vacantly absent rerun',
  exchange: 'reluctantly wealthy',
};

export const sampleWithNewData: NewStock = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
