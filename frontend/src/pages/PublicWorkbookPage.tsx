import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect } from 'react';

import { Button, MainHeader, PublicWorkbookList } from '../components';
import { usePublicSearch, useRouter } from '../hooks';

const PublicWorkbookPage = () => {
  const {
    searchKeyword,
    searchType,
    workbookSearchResult,
    searchForPublicWorkbook,
  } = usePublicSearch();

  const { routePrevPage } = useRouter();

  useEffect(() => {
    searchForPublicWorkbook({ keyword: searchKeyword, type: searchType });
  }, []);

  return (
    <>
      <MainHeader />
      <Container>
        {workbookSearchResult.length === 0 ? (
          <NoSearchResult>
            <div>검색 결과가 없어요.</div>
            <Button
              size="full"
              backgroundColor="gray_6"
              onClick={routePrevPage}
            >
              돌아가기
            </Button>
          </NoSearchResult>
        ) : (
          <Title>{searchKeyword} 검색 결과</Title>
        )}
        <PublicWorkbookList publicWorkbooks={workbookSearchResult} />
      </Container>
    </>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1rem;
    `}
`;

const NoSearchResult = styled.div`
  position: absolute;
  width: 80%;
  max-width: 20rem;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  text-align: center;

  & > div {
    margin-bottom: 1rem;
  }
`;

const Title = styled.h2`
  margin-top: 0.5rem;
  margin-bottom: 1rem;
  text-align: center;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;
export default PublicWorkbookPage;
