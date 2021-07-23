import { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { getAccessTokenAsync } from '../api';
import { ROUTE, STORAGE_KEY } from '../constants';
import { loginState } from '../recoil';
import { getSessionStorage, setLocalStorage } from '../utils';

const GITHUB_OAUTH_QUERY = 'code';

const GithubCallbackPage = () => {
  const location = useLocation();
  const setIsLogin = useSetRecoilState(loginState);
  const code = new URLSearchParams(location.search).get(GITHUB_OAUTH_QUERY);
  const history = useHistory();

  useEffect(() => {
    if (!code) return;

    (async () => {
      try {
        const accessToken = await getAccessTokenAsync(code);

        setIsLogin(true);
        setLocalStorage(STORAGE_KEY.TOKEN, accessToken);

        //TODO: 훅으로 빼는 방법 고민하기
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
