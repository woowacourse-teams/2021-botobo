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

export interface QuizSetting extends CategoryResponse {
  isChecked: boolean;
}
