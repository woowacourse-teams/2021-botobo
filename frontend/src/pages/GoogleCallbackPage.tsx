import { useHistory, useLocation } from 'react-router-dom';

import { getAccessTokenAsync } from '../api';
import { ROUTE, STORAGE_KEY } from '../constants';
import { getSessionStorage, setLocalStorage } from '../utils';

const GOOGLE_OAUTH_QUERY = 'code';

const GoogleCallbackPage = () => {
  const location = useLocation();
  const code = new URLSearchParams(location.search).get(GOOGLE_OAUTH_QUERY);
  const history = useHistory();

  if (!code) return null;

  const login = async () => {
    try {
      const accessToken = await getAccessTokenAsync('google', code);

      setLocalStorage(STORAGE_KEY.TOKEN, accessToken);

      //TODO: 훅으로 빼는 방법 고민하기
      history.push(
        getSessionStorage(STORAGE_KEY.REDIRECTED_PATH) ?? ROUTE.HOME.PATH
      );
    } catch (error) {
      //TODO: 에러 바운더리로 보내기
      console.error(error);
    }
  };

  login();

  return null;
};

export default GoogleCallbackPage;
