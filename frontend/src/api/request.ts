interface HttpRequest {
  method: 'GET' | 'POST' | 'DELETE' | 'PATCH';
  path: string;
  data?: {
    [key: string]: unknown;
  };
}

const request = async ({ method, path, data }: HttpRequest) => {
  const response = await fetch(`https://botobo.r-e.kr${path}`, {
    method,
    headers: {
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: data && JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(await response.text());
  }

  return await response.json();
};

export const REQUEST = {
  GET: async <T>({ path }: Pick<HttpRequest, 'path'>): Promise<T> =>
    await request({ method: 'GET', path }),
  POST: async <T>({
    path,
    data,
  }: Pick<HttpRequest, 'path' | 'data'>): Promise<T> =>
    await request({ method: 'POST', path, data }),
};
