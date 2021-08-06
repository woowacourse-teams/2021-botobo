import styled from '@emotion/styled';
import React, { useEffect, useRef } from 'react';

import { PublicWorkbookAsync } from '../api';
import { SEARCH_TYPE, STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { PublicWorkbookResponse } from '../types';
import { ValueOf } from '../types/utils';
import { setSessionStorage } from '../utils';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  publicWorkbooks: PublicWorkbookResponse[];
  startIndex: number;
  searchKeyword: string;
  searchType: ValueOf<typeof SEARCH_TYPE>;
  searchForPublicWorkbook: ({
    keyword,
    ...options
  }: PublicWorkbookAsync) => Promise<void>;
  onClickItem?: () => void;
}

const loadItemCount = 20;
let scrollObserver: IntersectionObserver;

const PublicWorkbookList = ({
  publicWorkbooks,
  startIndex,
  searchKeyword,
  searchType,
  searchForPublicWorkbook,
  onClickItem,
}: Props) => {
  const scrollTarget = useRef<HTMLLIElement>(null);

  const { routePublicCards } = useRouter();

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
    if (startIndex % loadItemCount === 0) return;

    scrollObserver.disconnect();
  }, [startIndex]);

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
              onClickItem?.();
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
