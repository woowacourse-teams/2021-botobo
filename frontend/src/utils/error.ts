import axios from 'axios';

import { getRefreshUserTokenAsync } from '../api/user';
import { ERROR_MESSAGE } from '../constants';

interface HandleAccessTokenRefreshError<Resolve, Reject, ReturnValue> {
  resolve: () => Promise<Resolve>;
  reject?: () => Promise<Reject>;
  returnValue?: ReturnValue;
}

export const isAccessTokenExpiredError = (error: unknown) => {
  if (!axios.isAxiosError(error)) return null;

  const errorCode = error.response?.data.code as keyof typeof ERROR_MESSAGE;

  return errorCode === 'A001' || errorCode === 'A002';
};

export const isAccessTokenRefreshError = (error: unknown) => {
  if (!axios.isAxiosError(error)) return null;

  const errorCode = error.response?.data.code as keyof typeof ERROR_MESSAGE;

  return errorCode === 'A008';
};

export const handleAccessTokenRefreshError = async <
  Resolve = unknown,
  Reject = unknown,
  ReturnValue = undefined
>({
  resolve,
  reject,
  returnValue,
}: HandleAccessTokenRefreshError<Resolve, Reject, ReturnValue>) => {
  try {
    await getRefreshUserTokenAsync();
    return await resolve();
  } catch (error) {
    console.error(error);

    reject?.();

    return returnValue as ReturnValue;
  }
};
