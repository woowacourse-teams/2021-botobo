import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import { workbookState } from '../recoil';
import useSnackbar from './useSnackbar';

const useMain = () => {
  const { data: workbooks, errorMessage } = useRecoilValue(workbookState);
  const showSnackbar = useSnackbar();

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { workbooks };
};

export default useMain;
