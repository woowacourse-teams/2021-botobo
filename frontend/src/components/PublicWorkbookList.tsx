import styled from '@emotion/styled';
import React, { useEffect, useRef } from 'react';
import { useRecoilValue } from 'recoil';

import { PublicWorkbookAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { searchKeywordState, searchTypeState } from '../recoil';
import { PublicWorkbookResponse } from '../types';
import { setSessionStorage } from '../utils';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  publicWorkbooks: PublicWorkbookResponse[];
  startIndex: number;
  searchForPublicWorkbook: ({
    keyword,
    ...options
  }: PublicWorkbookAsync) => Promise<void>;
}

const loadItemCount = 20;
let scrollObserver: IntersectionObserver;

const PublicWorkbookList = ({
  publicWorkbooks,
  startIndex,
  searchForPublicWorkbook,
}: Props) => {
  const scrollTarget = useRef<HTMLLIElement>(null);

  const searchKeyword = useRecoilValue(searchKeywordState);
  const searchType = useRecoilValue(searchTypeState);

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
