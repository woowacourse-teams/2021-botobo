import axios, { AxiosError } from 'axios';

import { ERROR_MESSAGE } from '../constants';
import {
  handleAccessTokenRefreshError,
  isAccessTokenExpiredError,
  isAccessTokenRefreshError,
} from '../utils/error';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';
import { useLogout } from '.';

const useErrorHandler = () => {
  const { routeLogin } = useRouter();
  const { logout } = useLogout();
  const showSnackbar = useSnackbar();

  const handler = async <T>(
    error: AxiosError | Error | unknown,
    onRefreshCallback?: () => Promise<T>
  ) => {
    if (!(error instanceof Error)) return;

    console.error(error.message);

    if (!axios.isAxiosError(error)) return;

    const errorCode = error.response?.data.code as keyof typeof ERROR_MESSAGE;

    if (isAccessTokenExpiredError(error)) {
      logout();
      routeLogin();

      showSnackbar({ message: ERROR_MESSAGE[errorCode] });

      return;
    }

    if (isAccessTokenRefreshError(error) && onRefreshCallback) {
      handleAccessTokenRefreshError({
        resolve: onRefreshCallback,
        reject: handler.bind(null, error),
      });

      return;
    }

    return showSnackbar({
      message: ERROR_MESSAGE[errorCode] ?? '알 수 없는 에러가 발생했어요.',
      type: 'error',
    });
  };

  return handler;
};

export default useErrorHandler;
