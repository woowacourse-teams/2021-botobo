import styled from '@emotion/styled';
import React, { useEffect, useRef } from 'react';

import { PublicWorkbookAsync } from '../api';
import { PublicSearchQueryReturnType } from '../hooks/usePublicSearchQuery';
import { PublicWorkbookResponse } from '../types';
import LoadingSpinner from './LoadingSpinner';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  query: PublicSearchQueryReturnType;
  isLoading: boolean;
  publicWorkbooks: PublicWorkbookResponse[];
  searchForPublicWorkbook: (
    { keyword, ...options }: PublicWorkbookAsync,
    isNew?: boolean
  ) => Promise<void>;
  routePublicCards: (id: number) => void;
}

const MAX_COUNT_WORKBOOK_ONE_TIME = 20;

const PublicWorkbookList = ({
  query,
  isLoading,
  publicWorkbooks,
  searchForPublicWorkbook,
  routePublicCards,
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
      {publicWorkbooks.map(({ id, name, cardCount, author }, index) => (
        <li ref={scrollTarget} key={index}>
          <PublicWorkbook
            name={name}
            cardCount={cardCount}
            author={author}
            onClick={() => routePublicCards(id)}
          />
        </li>
      ))}
      {isLoading && (
        <LoadingSpinnerWrapper>
          <LoadingSpinner />
        </LoadingSpinnerWrapper>
      )}
    </StyledUl>
  );
};

const StyledUl = styled.ul`
  position: relative;
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;

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
