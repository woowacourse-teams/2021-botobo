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

const PublicWorkbookList = ({
  publicWorkbooks,
  startIndex,
  searchForPublicWorkbook,
}: Props) => {
  const scrollTarget = useRef<HTMLLIElement>(null);

  const searchKeyword = useRecoilValue(searchKeywordState);
  const searchType = useRecoilValue(searchTypeState);
  const isLastItem = publicWorkbooks.length % loadItemCount !== 0;

  const { routePublicCards } = useRouter();

  useEffect(() => {
    if (!scrollTarget.current) return;

    const scrollObserver = new IntersectionObserver(
      async ([entry], observer) => {
        if (!entry.isIntersecting) return;
        if (!scrollTarget.current) return;

        observer.unobserve(entry.target);

        await searchForPublicWorkbook({
          keyword: searchKeyword,
          type: searchType,
          start: startIndex,
        });
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
