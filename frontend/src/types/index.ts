export interface CategoryResponse {
  id: number;
  name: string;
  description: string;
  cardCount: number;
  logoUrl: string;
}

export interface QuizResponse {
  id: number;
  question: string;
  answer: string;
  categoryName: string;
}

export interface CardResponse {
  id: number;
  question: string;
  answer: string;
  // isBookmark: boolean;
}

export interface CardsResponse {
  categoryName: string;
  cards: CardResponse[];
}
