import axios from 'axios';

import { getCookie } from './../utils';
import { STORAGE_KEY } from '../constants';

export const request = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}/api`,
});

const token = getCookie(STORAGE_KEY.TOKEN);

request.defaults.headers.common['Authorization'] = token
  ? `Bearer ${token}`
  : '';
