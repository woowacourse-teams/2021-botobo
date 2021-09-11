import { getUserInfoAsync, getWorkbooksAsync } from '../src/api';
import { request } from '../src/api/request';
import { STORAGE_KEY } from '../src/constants';
import { WorkbookState } from '../src/recoil/workbookState';
import { UserInfoResponse } from '../src/types';

const getCookie = (name: string, cookies: string) => {
  const key = `${name}=`;

  return cookies
    ?.split('; ')
    ?.find((cookie) => cookie.includes(key))
    ?.slice(name.length + 1);
};

export const getUserInfo = async (cookies: string) => {
  const token = getCookie(STORAGE_KEY.TOKEN, cookies);

  console.log(token);

  request.defaults.headers.get['Authorization'] = token
    ? `Bearer ${token}`
    : '';

  try {
    const userInfo = token ? await getUserInfoAsync() : null;
    return userInfo;
  } catch (error) {
    return null;
  }
};

export const getWorkbook = async (
  userInfo: UserInfoResponse | null
): Promise<WorkbookState> => {
  try {
    const workbook = userInfo ? await getWorkbooksAsync() : [];
    return {
      data: workbook,
      errorMessage: null,
    };
  } catch (error) {
    return {
      data: [],
      errorMessage: '문제집을 불러오지 못했어요.',
    };
  }
};
