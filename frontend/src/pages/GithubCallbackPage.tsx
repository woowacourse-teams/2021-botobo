import { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { getAccessTokenAsync } from '../api';
import { ROUTE, STORAGE_KEY } from '../constants';
import { loginState } from '../recoil';
import { getSessionStorage } from '../utils';

const GITHUB_OAUTH_QUERY = 'code';

const GithubCallbackPage = () => {
  const history = useHistory();
  const location = useLocation();
  const setIsLogin = useSetRecoilState(loginState);
  const code = new URLSearchParams(location.search).get(GITHUB_OAUTH_QUERY);

  useEffect(() => {
    if (!code) return;

    (async () => {
      try {
        await getAccessTokenAsync(code);

        setIsLogin(true);
        history.push(
          getSessionStorage(STORAGE_KEY.REDIRECTED_PATH) ?? ROUTE.HOME.PATH
        );
      } catch (error) {
        //TODO: 에러 바운더리로 보내기
        console.error(error);
      }
    })();
  }, [code, history]);

  return null;
};

export default GithubCallbackPage;
