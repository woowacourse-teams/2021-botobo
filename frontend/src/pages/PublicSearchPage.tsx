import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import { MainHeader, PublicSearchBar, Workbook } from '../components';
import { ROUTE } from '../constants';
import { useSnackbar } from '../hooks';
import { publicWorkbookState } from '../recoil';
import { WorkbookListStyle } from '../styles';
import { formatNewLine } from '../utils';
import PageTemplate from './PageTemplate';

const PublicSearchPage = () => {
  const { data: publicWorkbooks, errorMessage } =
    useRecoilValue(publicWorkbookState);
  const showSnackbar = useSnackbar();

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return (
    <>
      <MainHeader sticky={false} />
      <StyledPageTemplate isScroll={true}>
        <PublicSearchBar />
        <Title>다양한 문제집</Title>
        <Description>공유된 문제집을 랜덤으로 제공해 드려요.</Description>
        <StyledUl>
          {publicWorkbooks.map(
            ({ id, name, cardCount, author, heartCount, tags }) => (
              <li key={id}>
                <Workbook
                  name={name}
                  cardCount={cardCount}
                  author={author}
                  heartCount={heartCount}
                  tags={tags}
                  path={`${ROUTE.PUBLIC_CARDS.PATH}/${id}`}
                />
              </li>
            )
          )}
        </StyledUl>
        <Suggestion>
          {formatNewLine(
            '원하는 결과가 없으신가요? \n 검색을 통해 더 많은 문제집을 찾아보세요!'
          )}
        </Suggestion>
      </StyledPageTemplate>
    </>
  );
};

const StyledPageTemplate = styled(PageTemplate)`
  padding-top: 1rem;
`;

const Title = styled.h2`
  margin-bottom: 1rem;
`;

const Description = styled.div`
  margin-bottom: 1rem;
`;

const StyledUl = styled.ul`
  ${WorkbookListStyle};
  position: relative;

  & > li:last-of-type {
    margin-bottom: 1rem;
  }
`;

const Suggestion = styled.div`
  margin-top: 1rem;
  text-align: center;
`;

export default PublicSearchPage;
