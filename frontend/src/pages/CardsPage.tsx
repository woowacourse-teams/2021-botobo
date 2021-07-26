import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import { Button, QnACard } from '../components';
import { useCards, useRouter } from '../hooks';
import { CardResponse } from '../types';

interface Filter {
  [key: number]: (cards: CardResponse[]) => CardResponse[];
}

const filterByLatest = (cards: CardResponse[]) => cards;

// const filterByBookMark = (cards: CardResponse[]) =>
//   [...cards].sort((card1, card2) =>
//     card1.isBookmark === card2.isBookmark ? 0 : card1.isBookmark ? -1 : 1
//   );

const filter: Filter = {
  1: filterByLatest,
  // 2: filterByBookMark,
};

const filters = [
  { id: 1, name: '최신순' },
  { id: 2, name: '즐겨찾기 순' },
];

const CardsPage = () => {
  const { workbookName, cards } = useCards();
  const { routeCardAdd } = useRouter();
  const [currentFilterId, setCurrentFilterId] = useState(filters[0].id);

  return (
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
            onClick={() => setCurrentFilterId(id)}
          >
            {name}
          </Button>
        ))}
      </Filter>
      <Button size="full" backgroundColor="blue" onClick={routeCardAdd}>
        새로운 카드 추가하기
      </Button>
      <CardList>
        {filter[currentFilterId](cards).map(({ id, question, answer }) => (
          <li key={id}>
            <QnACard question={question} answer={answer} />
          </li>
        ))}
      </CardList>
    </Container>
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
