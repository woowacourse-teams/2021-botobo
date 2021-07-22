export interface AccessTokenResponse {
  accessToken: string;
}

export interface WorkbookResponse {
  id: number;
  name: string;
  description: string;
  cardCount: number;
  logoUrl: string;
}

export interface SharedWorkbookResponse extends WorkbookResponse {
  author: string;
}

export interface QuizResponse {
  id: number;
  question: string;
  answer: string;
  workbookName: string;
}

export interface CardResponse {
  id: number;
  question: string;
  answer: string;
}

export interface CardsResponse {
  workbookName: string;
  cards: CardResponse[];
}

export interface TagResponse {
  id: number;
  name: string;
}

export interface SharedCardsResponse {
  id: number;
  workbookName: string;
  cardCount: number;
  tags: TagResponse[];
  cards: CardResponse[];
}
