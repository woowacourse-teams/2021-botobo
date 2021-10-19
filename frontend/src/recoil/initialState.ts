const tagInitialState = {
  id: -1,
  name: '',
};

export const workbookInitialState = {
  id: -1,
  name: '',
  cardCount: -1,
  heartCount: 0,
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
  workbookOpened: false,
  heartCount: 0,
  tags: [],
  cards: [],
};

export const publicCardsInitialState = {
  ...cardsInitialState,
  cardCount: -1,
  heart: false,
};
