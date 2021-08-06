import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import UserIcon from '../assets/business-card.svg';
import TagIcon from '../assets/tag.svg';
import { SEARCH_TYPE } from '../constants';
import { Flex } from '../styles';
import { SearchKeywordResponse } from '../types';
import CardTemplate from './CardTemplate';

interface Props {
  searchItems: SearchKeywordResponse[];
  onClickSearchItem: () => void;
  type: typeof SEARCH_TYPE.TAG | typeof SEARCH_TYPE.USER;
}

const PublicSearchList = ({ searchItems, onClickSearchItem, type }: Props) => (
  <StyledUl>
    {searchItems.map(({ id, name }) => (
      <li key={id}>
        <SearchItem onClick={onClickSearchItem}>
          <IconWrapper>
            {type === 'tag' && <TagIcon width="1rem" height="1rem" />}
            {type === 'user' && <UserIcon width="1rem" height="1rem" />}
          </IconWrapper>
          <Name>{name}</Name>
        </SearchItem>
      </li>
    ))}
  </StyledUl>
);

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
`;

const SearchItem = styled(CardTemplate)`
  ${Flex({ items: 'center' })};
`;

const IconWrapper = styled.div`
  & > svg {
    vertical-align: middle;
  }
`;

const Name = styled.span`
  margin-left: 1rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

export default PublicSearchList;
