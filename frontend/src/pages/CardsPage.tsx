import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import FillHeartIcon from '../assets/heart-solid.svg';
import {
  Button,
  CardAddForm,
  KakaoShareButton,
  MainHeader,
  QnACard,
} from '../components';
import { theme } from '../constants';
import { useCard } from '../hooks';
import { Flex } from '../styles';
import { CardResponse } from '../types';
import CardsLoadable from './CardsLoadable';
import PageTemplate from './PageTemplate';

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
    workbookId,
    workbookName,
    workbookOpened,
    cards,
    heartCount,
    tags,
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
      <StyledPageTemplate isScroll={true}>
        <TopContent>
          <div>
            <WorkbookName>{workbookName}</WorkbookName>
            <WorkbookCards>
              {cards.length}개의 카드를 학습 중이에요.
            </WorkbookCards>
          </div>
          <RightContent>
            <Heart>
              <FillHeartIcon
                width="1.5rem"
                height="1.5rem"
                fill={theme.color.red}
              />
              <span>{heartCount}</span>
            </Heart>
            {workbookOpened && (
              <ShareButtonWrapper>
                <KakaoShareButton
                  title={workbookName}
                  path={`publicCards/${workbookId}`}
                />
              </ShareButtonWrapper>
            )}
          </RightContent>
        </TopContent>
        <TagList>
          {tags.map(({ id, name }) => (
            <TagWrapper key={id}>
              <Tag>
                <span>#</span>
                {name}
              </Tag>
            </TagWrapper>
          ))}
        </TagList>
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
      </StyledPageTemplate>
    </>
  );
};

const StyledPageTemplate = styled(PageTemplate)`
  position: relative;
`;

const TopContent = styled.div`
  ${Flex({ items: 'flex-start' })};
`;

const RightContent = styled.div`
  ${Flex()};
  margin-left: auto;
`;

const Heart = styled.div`
  ${Flex({ direction: 'column', items: 'center' })};

  & > span {
    margin-top: 0.25rem;
  }
`;

const ShareButtonWrapper = styled.div`
  margin-left: 0.5rem;
`;

const WorkbookName = styled.h2`
  word-break: break-all;
  margin-bottom: 0.5rem;
`;

const WorkbookCards = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.gray_6};
  `}
`;

const TagList = styled.ul`
  margin-top: 0.5rem;
`;

const TagWrapper = styled.li`
  display: inline;
  word-break: break-all;
`;

const Tag = styled.span`
  margin-right: 0.5rem;
  line-height: 1.5;

  ${({ theme }) => css`
    color: ${theme.color.gray_8};
  `};

  & > span {
    margin-right: 0.1rem;
  }
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
  grid-template-columns: repeat(1, 1fr);
  gap: 2rem;
  margin-top: 2rem;
`;

export default CardsPage;
