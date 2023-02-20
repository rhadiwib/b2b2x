export interface ITransaction {
  id?: number;
  transactionId?: string;
  channel?: string | null;
  statusCode?: string | null;
  statusDesc?: string | null;
}

export const defaultValue: Readonly<ITransaction> = {};
