import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react';

import { Button, MainHeader, PublicWorkbookList } from '../components';
import { SEARCH_CRITERIA } from '../constants';
import { usePublicSearch, useRouter } from '../hooks';
import { Flex } from '../styles';

const filters = [
  { id: 1, name: '최신순', criteria: SEARCH_CRITERIA.DATE },
  { id: 2, name: '좋아요 순', criteria: SEARCH_CRITERIA.LIKE },
  { id: 3, name: '이름 순', criteria: SEARCH_CRITERIA.NAME },
  { id: 4, name: '카드 개수 순', criteria: SEARCH_CRITERIA.COUNT },
];

let scrollObserver: IntersectionObserver;

const PublicWorkbookPage = () => {
  const {
    searchKeyword,
    searchType,
    workbookSearchResult,
    startIndex,
    setStartIndex,
    searchForPublicWorkbook,
  } = usePublicSearch();
  const [currentFilterId, setCurrentFilterId] = useState(filters[0].id);
  const scrollTarget = useRef<HTMLLIElement>(null);

  const { routePrevPage } = useRouter();

  useEffect(() => {
    searchForPublicWorkbook({ keyword: searchKeyword, type: searchType });
  }, [searchKeyword]);

  useEffect(() => {
    if (!scrollTarget.current) return;

    scrollObserver = new IntersectionObserver(
      (entries, observer) => {
        entries.forEach(async (entry) => {
          if (!entry.isIntersecting) return;
          if (!scrollTarget.current) return;

          await searchForPublicWorkbook({
            keyword: searchKeyword,
            type: searchType,
            start: startIndex,
          });

          observer.unobserve(entry.target);
          observer.observe(scrollTarget.current);
        });
      },
      {
        threshold: 0.1,
      }
    );

    scrollObserver.observe(scrollTarget.current);

    return () => scrollObserver?.disconnect();
  }, [scrollTarget.current]);

  useEffect(() => {
    if (startIndex % 20 === 0) return;

    scrollObserver.disconnect();
  }, [startIndex]);

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
          <>
            <Title>{searchKeyword} 검색 결과</Title>
            <Filter>
              {filters.map(({ id, name, criteria }) => (
                <Button
                  key={id}
                  shape="round"
                  backgroundColor={currentFilterId === id ? 'green' : 'gray_5'}
                  inversion={true}
                  onClick={() => {
                    if (id === currentFilterId) return;

                    setCurrentFilterId(id);
                    setStartIndex(0);
                    searchForPublicWorkbook({
                      keyword: searchKeyword,
                      type: searchType,
                      start: 0,
                      criteria,
                    });
                  }}
                >
                  {name}
                </Button>
              ))}
            </Filter>
            <PublicWorkbookList
              ref={scrollTarget}
              publicWorkbooks={workbookSearchResult}
            />
          </>
        )}
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

const Filter = styled.div`
  ${Flex()};
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1.5rem;
`;

export default PublicWorkbookPage;
