import axios, { Canceler } from 'axios';

import { getCookie } from './../utils';
import { STORAGE_KEY } from '../constants';

interface CancelController {
  [key: string]: Canceler | null;
}

export const request = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}`,
});

export const cancelController: CancelController = {
  searchCancel: null,
  filterCancel: null,
};

const token = getCookie(STORAGE_KEY.TOKEN);

request.defaults.headers.common['Authorization'] = token
  ? `Bearer ${token}`
  : '';
