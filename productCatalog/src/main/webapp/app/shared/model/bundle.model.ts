import dayjs from 'dayjs';
import { ICpqQuotedetails } from 'app/shared/model/cpq-quotedetails.model';

export interface IBundle {
  id?: number;
  bundleId?: string;
  bundleName?: string | null;
  quoteTemplateId?: number | null;
  isCompatible?: boolean | null;
  recurringAmount?: number | null;
  singleAmount?: number | null;
  usageAmount?: number | null;
  totalAmount?: number | null;
  createdAt?: string | null;
  createdBy?: string | null;
  quantity?: number | null;
  cpqQuotedetails?: ICpqQuotedetails | null;
}

export const defaultValue: Readonly<IBundle> = {
  isCompatible: false,
};
