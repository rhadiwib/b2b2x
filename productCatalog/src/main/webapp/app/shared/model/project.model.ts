import dayjs from 'dayjs';
import { ICpqQuotedetails } from 'app/shared/model/cpq-quotedetails.model';

export interface IProject {
  id?: number;
  projectId?: string;
  projectName?: string | null;
  discountTier?: string | null;
  companyName?: string | null;
  accountManager?: string | null;
  estQuantity?: number | null;
  contractStartPeriod?: string | null;
  contractEndPeriod?: string | null;
  cpqQuotedetails?: ICpqQuotedetails | null;
}

export const defaultValue: Readonly<IProject> = {};
