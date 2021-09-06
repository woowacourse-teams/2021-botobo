import axios, { AxiosError } from 'axios';

import { ERROR_MESSAGE } from '../constants';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const useErrorHandler = () => {
  const { routeLogin } = useRouter();
  const showSnackbar = useSnackbar();

  const handler = (error: AxiosError | Error | unknown) => {
    if (!(error instanceof Error)) return;

    console.error(error.message);

    if (!axios.isAxiosError(error)) return;

    const errorCode = error.response?.data.code as keyof typeof ERROR_MESSAGE;

    if (errorCode === 'A001' || errorCode === 'A002') {
      routeLogin();

      showSnackbar({ message: ERROR_MESSAGE[errorCode] });

      return;
    }

    showSnackbar({
      message: ERROR_MESSAGE[errorCode] ?? '알 수 없는 에러가 발생했어요.',
      type: 'error',
    });
  };

  return handler;
};

export default useErrorHandler;
