import styled from '@emotion/styled';
import React, { useEffect, useRef } from 'react';

import { PublicWorkbookAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { usePublicSearchQuery, useRouter } from '../hooks';
import { PublicWorkbookResponse } from '../types';
import { setSessionStorage } from '../utils';
import LoadingSpinner from './LoadingSpinner';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  isLoading: boolean;
  publicWorkbooks: PublicWorkbookResponse[];
  searchForPublicWorkbook: ({
    keyword,
    ...options
  }: PublicWorkbookAsync) => Promise<void>;
}

const PublicWorkbookList = ({
  isLoading,
  publicWorkbooks,
  searchForPublicWorkbook,
}: Props) => {
  const { keyword, tags, users } = usePublicSearchQuery();
  const { routePublicCards } = useRouter();

  const scrollTarget = useRef<HTMLLIElement>(null);

  useEffect(() => {
    if (!scrollTarget.current) return;

    const scrollObserver = new IntersectionObserver(
      async ([entry], observer) => {
        if (!entry.isIntersecting) return;
        if (!scrollTarget.current) return;

        observer.unobserve(entry.target);

        await searchForPublicWorkbook({ keyword, tags, users });
      },
      {
        threshold: 0.1,
      }
    );

    scrollObserver.observe(scrollTarget.current);

    return () => scrollObserver.disconnect();
  }, [scrollTarget.current]);

  return (
    <StyledUl>
      {publicWorkbooks.map(({ id, name, cardCount, author }) => (
        <li ref={scrollTarget} key={id}>
          <PublicWorkbook
            name={name}
            cardCount={cardCount}
            author={author}
            onClick={() => {
              setSessionStorage(STORAGE_KEY.PUBLIC_WORKBOOK_ID, id);
              routePublicCards();
            }}
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
