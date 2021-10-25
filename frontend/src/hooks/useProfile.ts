import { useRecoilState } from 'recoil';

import { postProfileImageAsync, putProfileAsync } from './../api';
import { userState } from './../recoil';
import { UserInfoResponse } from './../types';
import useErrorHandler from './useErrorHandler';
import useSnackbar from './useSnackbar';

const useProfile = () => {
  const [userInfo, setUserInfo] = useRecoilState(userState);

  const showSnackbar = useSnackbar();
  const errorHandler = useErrorHandler();

  const editProfile = async (userInfo: Omit<UserInfoResponse, 'id'>) => {
    try {
      const newUserInfo = await putProfileAsync(userInfo);

      setUserInfo(newUserInfo);
      showSnackbar({ message: '프로필이 수정되었어요.' });
    } catch (error) {
      errorHandler(error, editProfile.bind(null, userInfo));
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
      errorHandler(error, updateProfileUrl.bind(null, file));
    }
  };

  return { userInfo, updateProfileUrl, editProfile };
};

export default useProfile;
