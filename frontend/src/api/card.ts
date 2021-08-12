import { CardResponse, CardsResponse, PublicCardsResponse } from './../types';
import { request } from './request';

interface PostCardAsync {
  question: string;
  answer: string;
  workbookId: number;
}

export const getCardsAsync = async (workbookId: number) => {
  const { data } = await request.get<CardsResponse>(
    `/workbooks/${workbookId}/cards`
  );

  return data;
};

export const postCardAsync = async (params: PostCardAsync) => {
  const { data } = await request.post<CardResponse>('/cards', params);

  return data;
};

export const putCardAsync = async (cardInfo: CardResponse) => {
  const { id, ...params } = cardInfo;

  const { data } = await request.put<CardResponse>(`/cards/${id}`, params);

  return data;
};

export const deleteCardAsync = async (id: number) => {
  await request.delete(`/cards/${id}`);
};

export const getPublicCardsAsync = async (publicWorkbookId: number) => {
  const { data } = await request.get<PublicCardsResponse>(
    `/workbooks/public/${publicWorkbookId}`
  );

  return data;
};

export const postPublicCardsAsync = async (
  workbookId: number,
  cardIds: number[]
) => {
  const { data } = await request.post<PublicCardsResponse>(
    `/workbooks/${workbookId}/cards`,
    { cardIds }
  );

  return data;
};
