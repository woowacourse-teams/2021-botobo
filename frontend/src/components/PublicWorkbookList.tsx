import styled from '@emotion/styled';
import React, { useEffect, useRef } from 'react';

import { PublicWorkbookAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { usePublicSearchQuery, useRouter } from '../hooks';
import { PublicWorkbookResponse } from '../types';
import { setSessionStorage } from '../utils';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  publicWorkbooks: PublicWorkbookResponse[];
  searchForPublicWorkbook: ({
    keyword,
    ...options
  }: PublicWorkbookAsync) => Promise<void>;
}

const loadItemCount = 20;

const PublicWorkbookList = ({
  publicWorkbooks,
  searchForPublicWorkbook,
}: Props) => {
  const { keyword, type } = usePublicSearchQuery();
  const { routePublicCards } = useRouter();

  const scrollTarget = useRef<HTMLLIElement>(null);

  const isLastItem = publicWorkbooks.length % loadItemCount !== 0;

  useEffect(() => {
    if (!scrollTarget.current) return;

    const scrollObserver = new IntersectionObserver(
      async ([entry], observer) => {
        if (!entry.isIntersecting) return;
        if (!scrollTarget.current) return;

        observer.unobserve(entry.target);

        searchForPublicWorkbook({ keyword, type });
      },
      {
        threshold: 0.1,
      }
    );

    scrollObserver.observe(scrollTarget.current);

    if (isLastItem) {
      scrollObserver.disconnect();
    }

    return () => scrollObserver.disconnect();
  }, [scrollTarget.current, isLastItem]);

  return (
    <StyledUl>
      {publicWorkbooks.map(({ id, name, cardCount, author }, index) => (
        <li ref={scrollTarget} key={index}>
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
    </StyledUl>
  );
};

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
`;

export default PublicWorkbookList;
