import axios from 'axios';

import { STORAGE_KEY } from '../constants';
import { getLocalStorage } from '../utils';

export const request = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}/api`,
});

const token = getLocalStorage(STORAGE_KEY.TOKEN);

request.defaults.headers.common['Authorization'] = token
  ? `Bearer ${token}`
  : '';
