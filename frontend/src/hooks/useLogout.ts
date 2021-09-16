import { useSetRecoilState } from 'recoil';

import { postLogoutAsync } from '../api';
import { shouldWorkbookUpdateState, userState } from '../recoil';
import { useRouter } from '.';

const useLogout = () => {
  const { routeMain } = useRouter();
  const setUserInfo = useSetRecoilState(userState);
  const setIsWorkbookUpdate = useSetRecoilState(shouldWorkbookUpdateState);

  const logout = async ({ isRouteMain = false } = {}) => {
    try {
      await postLogoutAsync();
      setUserInfo(null);
      setIsWorkbookUpdate(true);
      isRouteMain && routeMain();
    } catch (error) {
      //TODO: 에러 바운더리로 보내기
      console.error(error);
    }
  };

  return { logout };
};

export default useLogout;
