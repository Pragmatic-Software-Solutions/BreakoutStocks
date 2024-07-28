import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 9869,
  login: 'o',
};

export const sampleWithPartialData: IUser = {
  id: 8415,
  login: '=kjp@QtTU',
};

export const sampleWithFullData: IUser = {
  id: 3178,
  login: '^HK@4N\\"Qa',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
