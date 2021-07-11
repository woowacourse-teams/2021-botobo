interface HttpRequest {
  method: 'GET' | 'POST' | 'DELETE' | 'PATCH';
  path: string;
}

const request = async ({ method, path }: HttpRequest) => {
  const response = await fetch(`https://botobo.r-e.kr${path}`, {
    method,
    headers: {
      'Content-Type': 'application/json; charset=UTF-8',
    },
  });

  if (!response.ok) {
    throw new Error(await response.text());
  }

  return await response.json();
};

export const REQUEST = {
  GET: async ({ path }: Pick<HttpRequest, 'path'>) =>
    await request({ method: 'GET', path }),
};
