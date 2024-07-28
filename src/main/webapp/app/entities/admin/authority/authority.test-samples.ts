import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '74ded38e-a80c-459b-b1fb-7720657f751f',
};

export const sampleWithPartialData: IAuthority = {
  name: '6bde8441-0b87-4b42-90f0-31b677c0ad27',
};

export const sampleWithFullData: IAuthority = {
  name: 'b15b138e-3686-461c-8a09-fc6ab2895af3',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
