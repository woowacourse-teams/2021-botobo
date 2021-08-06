import styled from '@emotion/styled';
import React, { forwardRef } from 'react';

import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { PublicWorkbookResponse } from '../types';
import { setSessionStorage } from '../utils';
import PublicWorkbook from './PublicWorkbook';

interface Props {
  publicWorkbooks: PublicWorkbookResponse[];
  onClickItem?: () => void;
}

const PublicWorkbookList = forwardRef<HTMLLIElement, Props>(
  ({ publicWorkbooks, onClickItem }: Props, ref) => {
    const { routePublicCards } = useRouter();

    return (
      <StyledUl>
        {publicWorkbooks.map(({ id, name, cardCount, author }, index) => (
          <li ref={ref} key={index}>
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
  }
);

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
`;

PublicWorkbookList.displayName = 'PublicWorkbookList';

export default PublicWorkbookList;
