import axios, { Canceler } from 'axios';

import { getCookie } from './../utils';
import { STORAGE_KEY } from '../constants';

interface CancelController {
  [key: string]: Canceler | null;
}

export const noAuthorization = { Authorization: '' };

export const request = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}`,
  withCredentials: true,
});

export const cancelController: CancelController = {
  searchCancel: null,
  filterCancel: null,
};

const token = getCookie(STORAGE_KEY.TOKEN);

if (token) {
  request.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}
