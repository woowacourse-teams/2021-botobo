import { rest } from 'msw';

const handlers = [
  rest.get(`http://localhost:8080/categories`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        categories: [
          {
            id: 1,
            name: 'Java',
            cardCount: 1,
          },
        ],
      })
    );
  }),
];

export default handlers;
