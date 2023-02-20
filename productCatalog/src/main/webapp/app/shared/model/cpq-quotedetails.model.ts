import dayjs from 'dayjs';
import { IProject } from 'app/shared/model/project.model';
import { IBundle } from 'app/shared/model/bundle.model';
import { IVersion } from 'app/shared/model/version.model';

export interface ICpqQuotedetails {
  id?: number;
  quoteId?: string;
  quoteStatus?: string | null;
  createdAt?: string | null;
  project?: IProject | null;
  bundles?: IBundle[] | null;
  versions?: IVersion[] | null;
}

export const defaultValue: Readonly<ICpqQuotedetails> = {};
