import { useHistory, useLocation } from 'react-router-dom';

import { getAccessTokenAsync } from '../api';
import { ROUTE, STORAGE_KEY } from '../constants';
import { AuthType } from '../types';
import { getSessionStorage } from '../utils';

const OAuthCallbackPage = () => {
  const { search } = useLocation();
  const type = getSessionStorage(STORAGE_KEY.SOCIAL_TYPE) as AuthType;
  const code = new URLSearchParams(search).get('code');
  const history = useHistory();

  if (!type || !code) return null;

  const login = async () => {
    try {
      await getAccessTokenAsync(type, code);

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

export default OAuthCallbackPage;
