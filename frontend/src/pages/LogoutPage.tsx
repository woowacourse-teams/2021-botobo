import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';

import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { userState } from '../recoil';
import { removeLocalStorage } from '../utils';

const LogoutPage = () => {
  const { routeMain } = useRouter();
  const setUserInfo = useSetRecoilState(userState);

  useEffect(() => {
    removeLocalStorage(STORAGE_KEY.TOKEN);
    setUserInfo(null);
    routeMain();
  }, []);

  return null;
};

export default LogoutPage;
