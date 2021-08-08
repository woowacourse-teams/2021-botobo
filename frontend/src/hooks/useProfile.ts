import { useRecoilState } from 'recoil';

import { putProfileAsync } from './../api/';
import { userState } from './../recoil';
import { UserInfoResponse } from './../types';
import useSnackbar from './useSnackbar';

const useProfile = () => {
  const [userInfo] = useRecoilState(userState);

  const showSnackbar = useSnackbar();

  const editProfile = async (userInfo: UserInfoResponse) => {
    try {
      await putProfileAsync(userInfo);
      showSnackbar({ message: '프로필이 수정되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '프로필을 수정하지 못했어요.', type: 'error' });
    }
  };

  return { userInfo, editProfile };
};

export default useProfile;
