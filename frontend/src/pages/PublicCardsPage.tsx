import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, Checkbox, PageHeader, PublicQnACard } from '../components';
import { ROUTE } from '../constants';
import { usePublicCard } from '../hooks';
import { Flex } from '../styles';

const PublicCardsPage = () => {
  const {
    workbookName,
    cardCount,
    tags,
    publicCards,
    isAllCardChecked,
    checkAllCard,
    checkedCardCount,
    checkCard,
  } = usePublicCard();

  return (
    <>
      <PageHeader
        title={ROUTE.PUBLIC_CARDS.TITLE}
        sticky={true}
        rightContent={
          <StyledButton size="full" shape="square" backgroundColor="blue">
            바로 풀어보기
          </StyledButton>
        }
      />
      <Container>
        <WorkbookName>{workbookName}</WorkbookName>
        <CardCount>{cardCount}개의 카드</CardCount>
        <TagList>
          {tags.map((tag) => (
            <li key={tag.id}>
              <Tag>#{tag.name}</Tag>
            </li>
          ))}
        </TagList>
        <ul>
          {publicCards.map(({ id, question, answer, isChecked }) => (
            <CardItem key={id}>
              <PublicQnACard
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

const TagList = styled.ul`
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

export default PublicCardsPage;
