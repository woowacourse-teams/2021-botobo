export const getSessionStorage = (key: string) => {
  if (typeof window === 'undefined') return;

  try {
    const item = window.sessionStorage.getItem(key) || JSON.stringify(null);

    return JSON.parse(item);
  } catch (error) {
    console.error(error);

    return '';
  }
};

export const setSessionStorage = (key: string, value: unknown) => {
  if (typeof window === 'undefined') return;

  try {
    window.sessionStorage.setItem(key, JSON.stringify(value));
  } catch (error) {
    console.error(error);
  }
};

export const getLocalStorage = (key: string) => {
  if (typeof window === 'undefined') return;

  try {
    const item = window.localStorage.getItem(key) || JSON.stringify(null);

    return JSON.parse(item);
  } catch (error) {
    console.error(error);

    return '';
  }
};

export const setLocalStorage = (key: string, value: unknown) => {
  if (typeof window === 'undefined') return;

  try {
    window.localStorage.setItem(key, JSON.stringify(value));
  } catch (error) {
    console.error(error);
  }
};

export const removeLocalStorage = (key: string) => {
  if (typeof window === 'undefined') return;

  window.localStorage.removeItem(key);
};
