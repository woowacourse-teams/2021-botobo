import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import { Button, CardAddForm, MainHeader, QnACard } from '../components';
import { useCard } from '../hooks';
import { CardResponse } from '../types';
import CardsLoadable from './CardsLoadable';

interface Filter {
  [key: number]: (cards: CardResponse[]) => CardResponse[];
}

const filterByLatest = (cards: CardResponse[]) => cards;

const filterByBookMark = (cards: CardResponse[]) =>
  [...cards].sort((card1, card2) =>
    card1.bookmark === card2.bookmark ? 0 : card1.bookmark ? -1 : 1
  );

const filter: Filter = {
  1: filterByLatest,
  2: filterByBookMark,
};

const filters = [
  { id: 1, name: '최신순' },
  { id: 2, name: '즐겨찾기 순' },
];

const CardsPage = () => {
  const {
    workbookName,
    cards,
    getCards,
    createCard,
    editCard,
    deleteCard,
    toggleBookmark,
    openModal,
    isLoading,
  } = useCard();
  const [currentFilterId, setCurrentFilterId] = useState(filters[0].id);

  if (isLoading) {
    return <CardsLoadable />;
  }

  return (
    <>
      <MainHeader />
      {cards.length > 0 && (
        <Container>
          <WorkbookName>{workbookName}</WorkbookName>
          <span>{cards.length}개의 카드를 학습 중이에요.</span>
          <Filter>
            {filters.map(({ id, name }) => (
              <Button
                key={id}
                shape="round"
                backgroundColor={currentFilterId === id ? 'green' : 'gray_5'}
                inversion={true}
                onClick={async () => {
                  if (id === currentFilterId) return;

                  await getCards();
                  setCurrentFilterId(id);
                }}
              >
                {name}
              </Button>
            ))}
          </Filter>
          <Button
            size="full"
            backgroundColor="blue"
            onClick={() =>
              openModal({
                content: <CardAddForm onSubmit={createCard} />,
                title: workbookName,
                closeIcon: 'back',
                type: 'full',
              })
            }
          >
            새로운 카드 추가하기
          </Button>
          <CardList>
            {filter[currentFilterId](cards).map((cardInfo) => (
              <li key={cardInfo.id}>
                <QnACard
                  cardInfo={cardInfo}
                  workbookName={workbookName}
                  editCard={editCard}
                  deleteCard={deleteCard}
                  toggleBookmark={toggleBookmark}
                />
              </li>
            ))}
          </CardList>
        </Container>
      )}
    </>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const WorkbookName = styled.h2`
  margin-bottom: 1rem;
`;

const Filter = styled.div`
  height: 3rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
  & > button {
    margin-right: 0.8rem;
  }
`;

const CardList = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 2rem;
  margin-top: 2rem;
`;

export default CardsPage;
