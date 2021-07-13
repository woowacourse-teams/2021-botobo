import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import searchCloseImage from '../assets/cross-mark.svg';
import searchImage from '../assets/search.svg';
import { Button, QnACard } from '../components';
import { Flex } from '../styles';
import { CardResponse } from '../types';

const data = {
  categoryName: '자바스크립트',
  cards: [
    {
      id: 1,
      question: '클로저란 무엇인가요 무엇인가요 무엇인가요 무엇인가요?',
      answer: '클로저란 어쩌고 저쩌고 입니다.',
      isBookmark: false,
    },
    {
      id: 2,
      question: '클로저란 무엇인가요?',
      answer: '클로저란 어쩌고 저쩌고 입니다.',
      isBookmark: true,
    },
    {
      id: 3,
      question: '클로저란 무엇인가요?',
      answer:
        '클로저란 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 입니다.',
      isBookmark: true,
    },
    {
      id: 4,
      question: '클로저란 무엇인가요?',
      answer: '클로저란 어쩌고 저쩌고 입니다.',
      isBookmark: false,
    },
  ],
};

interface Filter {
  [key: number]: (cards: CardResponse[]) => CardResponse[];
}

const filterByLatest = (cards: CardResponse[]) => cards;

const filterByBookMark = (cards: CardResponse[]) =>
  [...cards].sort((card1, card2) =>
    card1.isBookmark === card2.isBookmark ? 0 : card1.isBookmark ? -1 : 1
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
  const [currentFilterId, setCurrentFilterId] = useState(filters[0].id);
  const [isSearchMode, setIsSearchMode] = useState(false);
  const { categoryName, cards } = data;

  return (
    <Container>
      <CategoryName>{categoryName}</CategoryName>
      <span>{cards.length}개의 카드를 학습 중이에요.</span>
      <Controller>
        {isSearchMode ? (
          <SearchBar>
            <SearchImage src={searchImage} alt="검색 버튼" />
            <SearchInput />
            <button onClick={() => setIsSearchMode(false)}>
              <SearchCloseImage src={searchCloseImage} alt="검색창 닫기 버튼" />
            </button>
          </SearchBar>
        ) : (
          <>
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
            <button onClick={() => setIsSearchMode(true)}>
              <SearchImage src={searchImage} alt="검색" />
            </button>
          </>
        )}
      </Controller>
      <Button size="full">새로운 카드 추가하기</Button>
      <ul>
        {filter[currentFilterId](cards).map(
          ({ id, question, answer, isBookmark }) => (
            <li key={id}>
              <QnACard
                question={question}
                answer={answer}
                isBookmark={isBookmark}
              />
            </li>
          )
        )}
      </ul>
    </Container>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const CategoryName = styled.h2`
  margin-bottom: 1rem;
`;

const Controller = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  height: 3rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
`;

const Filter = styled.div`
  & > button {
    margin-right: 0.8rem;
  }
`;

const SearchBar = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })}
  width: 100%;
  height: 100%;
  padding: 0.5rem 1rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${theme.color.gray_9};
  `};

  & > img {
    cursor: text;
  }
`;

const SearchInput = styled.input`
  width: 100%;
  height: 100%;
  outline: none;
  border: none;
  margin: 0 0.5rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

const SearchImage = styled.img`
  width: 1.3rem;
  height: 1.3rem;
`;

const SearchCloseImage = styled.img`
  width: 1rem;
  height: 1rem;
`;

export default CardsPage;
