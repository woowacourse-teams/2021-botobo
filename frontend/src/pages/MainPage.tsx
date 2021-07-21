import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import PlusIcon from '../assets/plus.svg';
import { Button, CategoryList, QuizStarter } from '../components';
import { ROUTE } from '../constants';
import { useMain } from '../hooks';
import { Flex } from '../styles';

const MainPage = () => {
  const { categories } = useMain();
  const history = useHistory();

  return (
    <Container>
      <QuizStarter />
      <section>
        <CategoryHeader>
          <CategoryTitle>학습 중</CategoryTitle>
          <Button
            shape="circle"
            backgroundColor="white"
            color="green"
            hasShadow={true}
          >
            <StyledPlusIcon />
          </Button>
        </CategoryHeader>
        <CategoryList
          categories={categories}
          onClickCategory={(categoryId) =>
            history.push(`${ROUTE.CARDS.PATH}?categoryId=${categoryId}`)
          }
        />
      </section>
    </Container>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const CategoryHeader = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-top: 3rem;
`;

const CategoryTitle = styled.h2`
  ${({ theme }) =>
    css`
      font-size: ${theme.fontSize.semiLarge};
    `};
`;

// TODO: 타입 체크하기
const StyledPlusIcon = styled(PlusIcon)`
  width: 1rem;
  height: 1rem;
  vertical-align: middle;

  ${({ theme }) =>
    css`
      fill: ${theme.color.green};
    `}
`;

export default MainPage;
