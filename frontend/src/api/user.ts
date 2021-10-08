import { removeCookie, setCookie } from './../utils';
import { STORAGE_KEY } from '../constants';
import { AccessTokenResponse, AuthType, UserInfoResponse } from '../types';
import { request } from './request';

interface PutHeartAsync {
  heart: boolean;
}

export const getAccessTokenAsync = async (
  socialType: AuthType,
  code: string
) => {
  const {
    data: { accessToken },
  } = await request.post<AccessTokenResponse>(`/login/${socialType}`, { code });

  setCookie(STORAGE_KEY.TOKEN, accessToken, 3600);

  request.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

  return accessToken;
};

export const getRefreshUserTokenAsync = async () => {
  const {
    data: { accessToken },
  } = await request.get<AccessTokenResponse>(`/token`);

  setCookie(STORAGE_KEY.TOKEN, accessToken, 3600);

  request.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

  return accessToken;
};

export const getUserInfoAsync = async () => {
  const { data } = await request.get<UserInfoResponse>('/users/me');

  return data;
};

export const getLogoutAsync = async () => {
  await request.get('/logout');

  removeCookie(STORAGE_KEY.TOKEN);

  request.defaults.headers.common['Authorization'] = '';
};

export const putProfileAsync = async (
  userInfo: Omit<UserInfoResponse, 'id'>
) => {
  const { data } = await request.put(`/users/me`, userInfo);

  return data;
};

export const postProfileImageAsync = async (formData: FormData) => {
  const { data } = await request.post<Pick<UserInfoResponse, 'profileUrl'>>(
    '/users/profile',
    formData
  );

  return data;
};

export const postUserNameCheckAsync = async (userName: string) => {
  await request.post('/users/name-check', { userName });
};

export const putHeartAsync = async (id: number) => {
  const { data } = await request.put<PutHeartAsync>(`workbooks/${id}/hearts`);

  return data;
};
