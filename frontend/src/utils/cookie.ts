export const setCookie = (name: string, value = '', maxAge: number) => {
  if (typeof document === 'undefined') return;

  document.cookie = `${name}=${value}; max-age=${maxAge}; secure; path=/;`;
};

export const getCookie = (name: string) => {
  if (typeof document === 'undefined') return;

  const key = `${name}=`;
  const cookies = document.cookie.split('; ');

  return cookies.find((cookie) => cookie.includes(key))?.slice(name.length + 1);
};

export const removeCookie = (name: string) => {
  if (typeof document === 'undefined') return;

  document.cookie = `${name}=; max-age=0;`;
};
