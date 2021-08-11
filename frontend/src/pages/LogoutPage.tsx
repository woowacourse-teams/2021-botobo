import { useSetRecoilState } from 'recoil';

import { postLogoutAsync } from '../api';
import { useRouter } from '../hooks';
import { userState } from '../recoil';

const LogoutPage = () => {
  const { routeMain } = useRouter();
  const setUserInfo = useSetRecoilState(userState);

  const logout = async () => {
    try {
      await postLogoutAsync();
      setUserInfo(null);
      routeMain();
    } catch (error) {
      //TODO: 에러 바운더리로 보내기
      console.error(error);
    }
  };

  logout();

  return null;
};

export default LogoutPage;
