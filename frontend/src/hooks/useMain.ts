import { useEffect } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { userState } from './../recoil/userState';
import { workbookIdState, workbookState } from '../recoil';
import useSnackbar from './useSnackbar';

const useMain = () => {
  const { data: workbooks, errorMessage } = useRecoilValue(workbookState);
  const userInfo = useRecoilValue(userState);
  const showSnackbar = useSnackbar();
  const setWorkbookId = useSetRecoilState(workbookIdState);

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { workbooks, userInfo, setWorkbookId };
};

export default useMain;
