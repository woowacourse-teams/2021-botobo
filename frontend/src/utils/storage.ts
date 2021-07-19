export const getSessionStorage = (key: string) => {
  try {
    const item = window.sessionStorage.getItem(key) || '';

    return JSON.parse(item);
  } catch (error) {
    console.error(error);

    return '';
  }
};

export const setSessionStorage = (key: string, value: unknown) => {
  try {
    window.sessionStorage.setItem(key, JSON.stringify(value));
  } catch (error) {
    console.error(error);
  }
};
