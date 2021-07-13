import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { Button, CategoryList, QuizStarter } from '../components';
import { ROUTE } from '../constants';
import { categoryState } from '../recoil';
import { Flex } from '../styles';

const MainPage = () => {
  const categories = useRecoilValue(categoryState);
  const history = useHistory();

  return (
    <Container>
      <QuizStarter />
      <section>
        <CategoryHeader>
          <CategoryTitle>전체 카테고리</CategoryTitle>
          <Button
            shape="circle"
            backgroundColor="white"
            color="green"
            hasShadow={true}
          >
            <i className="fas fa-plus"></i>
          </Button>
        </CategoryHeader>
        <CategoryList
          categories={categories}
          onClickCategory={(categoryId) =>
            history.push(`/${categoryId}${ROUTE.CARDS}`)
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
  ${Flex({ justify: 'space-between', items: 'center' })}
  margin-top: 3rem;
`;

const CategoryTitle = styled.h2`
  font-size: ${({ theme }) => theme.fontSize.semiLarge};
`;

export default MainPage;
