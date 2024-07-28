import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 5997,
  login: 'b^7$4N@j\\+D2\\PTMPRHq\\-4eeVNK\\\\t\\bP-s0OB',
};

export const sampleWithPartialData: IUser = {
  id: 18308,
  login: '+d^0M@YeZ\\gvr-qrs\\JC',
};

export const sampleWithFullData: IUser = {
  id: 23924,
  login: 'KP+*k@zYaW\\[9M9\\jR',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
