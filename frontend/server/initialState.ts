import { Response } from 'express';

import {
  getSearchKeywordRankingsAsync,
  getUserInfoAsync,
  getWorkbookRankingsAsync,
  getWorkbooksAsync,
} from '../src/api';
import { request } from '../src/api/request';
import { getRefreshTokenWithSsr } from '../src/api/user';
import { STORAGE_KEY } from '../src/constants';
import { WorkbookState } from '../src/recoil/workbookState';
import { UserInfoResponse } from '../src/types';
import { isAccessTokenRefreshError } from '../src/utils/error';

const getCookie = (name: string, cookies: string) => {
  const key = `${name}=`;

  return cookies
    ?.split('; ')
    ?.find((cookie) => cookie.includes(key))
    ?.slice(name.length + 1);
};

const setHeaderCookie = (accessToken: string, refreshToken?: string) => {
  request.defaults.headers.get['Authorization'] = `Bearer ${accessToken}`;

  if (refreshToken) {
    request.defaults.headers.Cookie = `${STORAGE_KEY.REFRESH_TOKEN}=${refreshToken}`;
  }
};

export const initRequest = (cookies: string) => {
  request.defaults.headers = {};

  const accessToken = getCookie(STORAGE_KEY.TOKEN, cookies);
  const refreshToken = getCookie(STORAGE_KEY.REFRESH_TOKEN, cookies);

  if (!accessToken) return;

  setHeaderCookie(accessToken, refreshToken);
};

const setToken = async (res: Response) => {
  try {
    const { accessToken, refreshTokenCookieInfo } =
      await getRefreshTokenWithSsr();

    const refreshToken = getCookie(
      STORAGE_KEY.REFRESH_TOKEN,
      refreshTokenCookieInfo
    );

    res.setHeader('Set-Cookie', [
      `${STORAGE_KEY.TOKEN}=${accessToken}; Max-Age=3600; Secure; Path=/;`,
      refreshTokenCookieInfo,
    ]);

    setHeaderCookie(accessToken, refreshToken);
  } catch (error) {
    console.error(error);
  }
};

export const getUserInfo = async (
  res: Response
): Promise<UserInfoResponse | null> => {
  try {
    return await getUserInfoAsync();
  } catch (error) {
    if (!isAccessTokenRefreshError(error)) return null;

    await setToken(res);

    return await getUserInfo(res);
  }
};

export const getWorkbook = async (
  userInfo: UserInfoResponse | null,
  res: Response
): Promise<WorkbookState> => {
  try {
    const workbook = userInfo ? await getWorkbooksAsync() : [];
    return {
      data: workbook,
      errorMessage: null,
    };
  } catch (error) {
    if (!isAccessTokenRefreshError(error)) {
      return {
        data: [],
        errorMessage: '문제집을 불러오지 못했어요.',
      };
    }

    await setToken(res);

    return await getWorkbook(userInfo, res);
  }
};

export const getWorkbookRankings = async () => {
  try {
    return await getWorkbookRankingsAsync();
  } catch (error) {
    return [];
  }
};

export const getSearchKeywordRankings = async () => {
  try {
    return await getSearchKeywordRankingsAsync();
  } catch (error) {
    return [];
  }
};
