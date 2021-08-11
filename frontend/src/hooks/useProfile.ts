import { useRecoilState } from 'recoil';

import { postProfileImageAsync, putProfileAsync } from './../api';
import { userState } from './../recoil';
import { UserInfoResponse } from './../types';
import useSnackbar from './useSnackbar';

const useProfile = () => {
  const [userInfo, setUserInfo] = useRecoilState(userState);

  const showSnackbar = useSnackbar();

  const editProfile = async (userInfo: Omit<UserInfoResponse, 'id'>) => {
    try {
      const newUserInfo = await putProfileAsync(userInfo);

      setUserInfo(newUserInfo);
      showSnackbar({ message: '프로필이 수정되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '프로필을 수정하지 못했어요.', type: 'error' });
    }
  };

  const updateProfileUrl = async (file?: File) => {
    if (!userInfo) return;
    const formData = new FormData();

    if (file) {
      formData.append('profile', file);
    }

    try {
      const newImage = await postProfileImageAsync(formData);

      setUserInfo({ ...userInfo, profileUrl: newImage.profileUrl });
    } catch (error) {
      console.error(error);
    }
  };

  return { userInfo, updateProfileUrl, editProfile };
};

export default useProfile;
