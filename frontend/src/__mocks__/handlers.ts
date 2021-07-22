import { rest } from 'msw';

const baseURL = `${process.env.REACT_APP_SERVER_URL}/api`;

const handlers = [
  rest.get(`${baseURL}/workbooks`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json([
        {
          id: 1,
          name: 'Java',
          cardCount: 1,
        },
      ])
    );
  }),
];

export default handlers;
