import dayjs from 'dayjs';

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
}

export const defaultValue: Readonly<IProject> = {};
