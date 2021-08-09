export interface AccessTokenResponse {
  accessToken: string;
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
  tags: TagResponse[];
}

export interface PublicWorkbookResponse
  extends Omit<WorkbookResponse, 'opened'> {
  author: string;
  heart: number;
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
  cards: CardResponse[];
}

export interface PublicCardsResponse extends CardsResponse {
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
}
