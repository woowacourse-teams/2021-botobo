import styled from '@emotion/styled';
import React from 'react';

import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { PublicWorkbookResponse } from '../types';
import { setSessionStorage } from '../utils';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  publicWorkbooks: PublicWorkbookResponse[];
  onClickItem?: () => void;
}

const PublicWorkbookList = ({ publicWorkbooks, onClickItem }: Props) => {
  const { routePublicCards } = useRouter();

  return (
    <StyledUl>
      {publicWorkbooks.map(({ id, name, cardCount, author }) => (
        <li key={id}>
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
