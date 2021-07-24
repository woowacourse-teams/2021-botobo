import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import { userState } from './../recoil/userState';
import { workbookState } from '../recoil';
import useSnackbar from './useSnackbar';

const useMain = () => {
  const { data: workbooks, errorMessage } = useRecoilValue(workbookState);
  const userInfo = useRecoilValue(userState);
  const showSnackbar = useSnackbar();

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { workbooks, userInfo };
};

export default useMain;
