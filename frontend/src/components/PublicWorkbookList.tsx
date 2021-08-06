import styled from '@emotion/styled';
import React from 'react';

import { PublicWorkbookResponse, SearchKeywordResponse } from '../types';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  onClickPublicWorkbook: (id: number) => void;
  searchList: PublicWorkbookResponse[] | SearchKeywordResponse[];
}

const isPublicWorkbook = (
  data: PublicWorkbookResponse[] | SearchKeywordResponse[]
): data is PublicWorkbookResponse[] => {
  const [response] = data;

  return 'cardCount' in response && 'author' in response;
};

const PublicWorkbookList = ({ onClickPublicWorkbook, searchList }: Props) => {
  if (searchList.length === 0) {
    return null;
  }

  return (
    <StyledUl>
      {isPublicWorkbook(searchList) &&
        searchList.map(({ id, name, cardCount, author }) => (
          <li key={id}>
            <PublicWorkbook
              name={name}
              cardCount={cardCount}
              author={author}
              onClick={() => onClickPublicWorkbook(id)}
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
