export interface AccessTokenResponse {
  accessToken: string;
}

export interface WorkbookResponse {
  id: number;
  name: string;
  cardCount: number;
}

export interface PublicWorkbookResponse extends WorkbookResponse {
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
  bookmark: boolean;
  encounterCount: number;
}

export interface CardsResponse {
  workbookId: number;
  workbookName: string;
  cards: CardResponse[];
}

export interface TagResponse {
  id: number;
  name: string;
}

export interface PublicCardsResponse {
  id: number;
  workbookName: string;
  cardCount: number;
  tags: TagResponse[];
  cards: CardResponse[];
}

export interface UserInfoResponse {
  id: number;
  userName: string;
  profileUrl: string;
}
