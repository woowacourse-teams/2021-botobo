import styled from '@emotion/styled';
import React, { useEffect, useRef } from 'react';

import { PublicWorkbookAsync } from '../api';
import { ROUTE } from '../constants';
import { PublicSearchQueryReturnType } from '../hooks/usePublicSearchQuery';
import { WorkbookListStyle } from '../styles';
import { PublicWorkbookResponse } from '../types';
import LoadingSpinner from './LoadingSpinner';
import Workbook from './Workbook';

interface Props {
  query: PublicSearchQueryReturnType;
  isLoading: boolean;
  publicWorkbooks: PublicWorkbookResponse[];
  searchForPublicWorkbook: (
    { keyword, ...options }: PublicWorkbookAsync,
    isNew?: boolean
  ) => Promise<void>;
}

const MAX_COUNT_WORKBOOK_ONE_TIME = 20;

const PublicWorkbookList = ({
  query,
  isLoading,
  publicWorkbooks,
  searchForPublicWorkbook,
}: Props) => {
  const { tags, users, criteria } = query;

  const scrollTarget = useRef<HTMLLIElement>(null);

  useEffect(() => {
    if (!scrollTarget.current) return;

    const scrollObserver = new IntersectionObserver(
      async ([entry], observer) => {
        if (!entry.isIntersecting) return;
        if (!scrollTarget.current) return;

        observer.unobserve(entry.target);

        if (publicWorkbooks.length < MAX_COUNT_WORKBOOK_ONE_TIME) return;

        await searchForPublicWorkbook(query, false);
      },
      {
        threshold: 0.1,
      }
    );

    scrollObserver.observe(scrollTarget.current);

    return () => scrollObserver.disconnect();
  }, [scrollTarget.current, tags, users, criteria]);

  return (
    <StyledUl>
      {publicWorkbooks.map(
        ({ id, name, cardCount, author, heartCount, tags }, index) => (
          <li ref={scrollTarget} key={index}>
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
      {isLoading && (
        <LoadingSpinnerWrapper>
          <LoadingSpinner />
        </LoadingSpinnerWrapper>
      )}
    </StyledUl>
  );
};

const StyledUl = styled.ul`
  ${WorkbookListStyle};
  position: relative;

  & > li:last-of-type {
    margin-bottom: 1rem;
  }
`;

const LoadingSpinnerWrapper = styled.div`
  position: absolute;
  width: 100%;
  bottom: -2.2rem;
`;

export default PublicWorkbookList;
