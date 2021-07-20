import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import { categoryState } from '../recoil';
import useSnackbar from './useSnackbar';

const useMain = () => {
  const { data: categories, errorMessage } = useRecoilValue(categoryState);
  const showSnackbar = useSnackbar();

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { categories };
};

export default useMain;
