import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';

import { Button, Checkbox, PageHeader, SharedQnACard } from '../components';
import { ROUTE } from '../constants';
import { Flex } from '../styles';

const data = {
  workbookName: 'Java',
  cardCount: 4,
  tags: ['java', '자바', '피케이'],
  cards: [
    {
      id: 1,
      question: '자바란 무엇인가요?',
      answer: '자바란 어쩌고 저쩌고입니다.',
    },
    {
      id: 2,
      question: '자바란 무엇인가요?',
      answer: '자바란 어쩌고 저쩌고입니다.자바란 어쩌고 저쩌고입니다.',
    },
    {
      id: 3,
      question: '자바란 무엇인가요?',
      answer:
        '자바란 어쩌고 저쩌고입니다.자바란 어쩌고 저쩌고입니다.자바란 어쩌고 저쩌고입니다.',
    },
    {
      id: 4,
      question: '자바란 무엇인가요? 자바란 무엇인가요? 자바란 무엇인가요?',
      answer:
        '자바란 어쩌고 저쩌고입니다.자바란 어쩌고 저쩌고입니다.자바란 어쩌고 저쩌고입니다.',
    },
  ],
};

const SharedCardsPage = () => {
  const [cards, setCards] = useState(
    data.cards.map((card) => ({ ...card, isChecked: false }))
  );
  const [isAllCardChecked, setIsAllCardChecked] = useState(false);

  const checkedCardCount = cards.filter(({ isChecked }) => isChecked).length;

  const checkCard = (id: number) => {
    const newCards = cards.map((card) => {
      if (card.id !== id) return card;

      return {
        ...card,
        isChecked: !card.isChecked,
      };
    });

    setCards(newCards);
  };

  const checkAllCard: React.ChangeEventHandler<HTMLInputElement> = ({
    target,
  }) => {
    setIsAllCardChecked(target.checked);
    setCards(cards.map((card) => ({ ...card, isChecked: target.checked })));
  };

  useEffect(() => {
    setIsAllCardChecked(checkedCardCount === data.cardCount);
  }, [checkedCardCount]);

  return (
    <>
      <PageHeader
        title={ROUTE.SHARED_CARDS.TITLE}
        sticky={true}
        rightContent={
          <StyledButton size="full" shape="square" backgroundColor="blue">
            바로 풀어보기
          </StyledButton>
        }
      />
      <Container>
        <WorkbookName>{data.workbookName}</WorkbookName>
        <CardCount>{data.cardCount}개의 카드</CardCount>
        <TagList>
          {data.tags.map((tag, index) => (
            <li key={index}>
              <Tag>#{tag}</Tag>
            </li>
          ))}
        </TagList>
        <ul>
          {cards.map(({ id, question, answer, isChecked }) => (
            <CardItem key={id}>
              <SharedQnACard
                question={question}
                answer={answer}
                isChecked={isChecked}
                onClick={() => checkCard(id)}
              />
            </CardItem>
          ))}
        </ul>
        <BottomContent>
          <CheckboxWrapper>
            <Checkbox
              labelText="전체"
              name="checkAll"
              checked={isAllCardChecked}
              onChange={checkAllCard}
            />
          </CheckboxWrapper>
          <Button size="full" shape="rectangle">
            <span>문제집으로 가져가기 ({checkedCardCount})</span>
          </Button>
        </BottomContent>
      </Container>
    </>
  );
};

const StyledButton = styled(Button)`
  width: 8rem;
`;

const Container = styled.div`
  margin-bottom: 3rem;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1.5rem;
    `}
`;

const WorkbookName = styled.h2`
  margin-bottom: 0.5rem;
`;

const CardCount = styled.div`
  margin-bottom: 0.5rem;

  ${({ theme }) => css`
    color: ${theme.color.gray_6};
  `};
`;

const TagList = styled.li`
  ${Flex({ items: 'center' })};
`;

const Tag = styled.button`
  margin-right: 0.5rem;

  ${({ theme }) => css`
    color: ${theme.color.blue};
    font-size: ${theme.fontSize.default};
  `};
`;

const CardItem = styled.li`
  margin-top: 1rem;

  &:not(:first-of-type) {
    margin-top: 2rem;
  }
`;

const BottomContent = styled.div`
  ${Flex()};
  opacity: 0.9;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
`;

const CheckboxWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 20%;
  min-width: 6rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `};
`;

export default SharedCardsPage;
