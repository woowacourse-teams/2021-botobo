import { useContext } from 'react';

import { SnackbarContext } from './../contexts/SnackbarProvider';

const useSnackbar = () => {
  const showSnackbar = useContext(SnackbarContext);

  if (typeof showSnackbar !== 'function') {
    throw new ReferenceError('해당 컨텍스트가 존재하지 않습니다.');
  }

  return showSnackbar;
};

export default useSnackbar;
