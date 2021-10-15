export interface AccessTokenResponse {
  accessToken: string;
}

export interface RefreshTokenWithSsr {
  refreshTokenCookieInfo: string;
}

export interface TagResponse {
  id: number;
  name: string;
}

export interface WorkbookResponse {
  id: number;
  name: string;
  cardCount: number;
  opened: boolean;
  heartCount: number;
  tags: TagResponse[];
}

export interface PublicWorkbookResponse
  extends Omit<WorkbookResponse, 'opened'> {
  author: string;
}

export interface QuizResponse {
  id: number;
  question: string;
  answer: string;
  encounterCount: number;
  workbookName: string;
}

export interface CardResponse {
  id: number;
  workbookId: number;
  question: string;
  answer: string;
  bookmark: boolean;
  encounterCount: number;
  nextQuiz: boolean;
}

export interface CardsResponse {
  workbookId: number;
  workbookName: string;
  workbookOpened: boolean;
  heartCount: number;
  tags: TagResponse[];
  cards: CardResponse[];
}

export interface PublicCardsResponse extends CardsResponse {
  heart: boolean;
  heartCount: number;
  cardCount: number;
  tags: TagResponse[];
}

export interface SearchKeywordResponse {
  id: number;
  name: string;
}

export interface UserInfoResponse {
  id: number;
  userName: string;
  profileUrl: string;
  bio: string;
}

export type AuthType = 'github' | 'google';
