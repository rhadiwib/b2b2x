import { ICpqQuotedetails } from 'app/shared/model/cpq-quotedetails.model';

export interface IVersion {
  id?: number;
  versionId?: number;
  versionNumber?: string | null;
  active?: boolean | null;
  cpqQuotedetails?: ICpqQuotedetails | null;
}

export const defaultValue: Readonly<IVersion> = {
  active: false,
};
