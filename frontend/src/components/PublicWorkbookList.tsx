import styled from '@emotion/styled';
import React from 'react';

import { PublicWorkbookResponse } from '../types';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  onClickPublicWorkbook: (id: number) => void;
  publicWorkbooks: PublicWorkbookResponse[];
}

const PublicWorkbookList = ({
  onClickPublicWorkbook,
  publicWorkbooks,
}: Props) => (
  <StyledUl>
    {publicWorkbooks.map(({ id, name, cardCount, author }) => (
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
const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
`;

export default PublicWorkbookList;
