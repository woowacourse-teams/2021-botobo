import { useRecoilValue } from 'recoil';

import { userState } from './../recoil/userState';

//TODO: 에러 처리
const useMain = () => {
  const userInfo = useRecoilValue(userState);

  return { userInfo };
};

export default useMain;
