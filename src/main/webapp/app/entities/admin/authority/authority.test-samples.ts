import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '0e14ddc3-cdf5-4a79-93bc-47824eab5488',
};

export const sampleWithPartialData: IAuthority = {
  name: 'a27e0479-4b68-4190-b853-97557f4466ba',
};

export const sampleWithFullData: IAuthority = {
  name: 'ef4fb923-a5fa-4f73-a2bb-8109617e286d',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
