const tagInitialState = {
  id: -1,
  name: '',
};

export const workbookInitialState = {
  id: -1,
  name: '',
  cardCount: -1,
  opened: true,
  tags: [tagInitialState],
};

export const cardInitialState = {
  id: -1,
  workbookId: -1,
  question: '',
  answer: '',
  bookmark: false,
  encounterCount: -1,
  nextQuiz: false,
};

export const cardsInitialState = {
  workbookId: -1,
  workbookName: '',
  cards: [cardInitialState],
};

export const publicCardsInitialState = {
  ...cardsInitialState,
  cardCount: -1,
  tags: [tagInitialState],
};
