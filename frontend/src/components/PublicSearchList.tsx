import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useRecoilValue } from 'recoil';

import UserIcon from '../assets/business-card.svg';
import TagIcon from '../assets/tag.svg';
import { useRouter } from '../hooks';
import { searchTypeState } from '../recoil';
import { Flex } from '../styles';
import { SearchKeywordResponse } from '../types';
import CardTemplate from './CardTemplate';

interface Props {
  searchItems: SearchKeywordResponse[];
}

const PublicSearchList = ({ searchItems }: Props) => {
  const { routePublicWorkbook } = useRouter();
  const searchType = useRecoilValue(searchTypeState);

  return (
    <StyledUl>
      {searchItems.map(({ id, name }) => (
        <li key={id}>
          <SearchItem
            onClick={() => {
              routePublicWorkbook();
            }}
          >
            <IconWrapper>
              {searchType === 'tag' && <TagIcon width="1rem" height="1rem" />}
              {searchType === 'user' && <UserIcon width="1rem" height="1rem" />}
            </IconWrapper>
            <Name>{name}</Name>
          </SearchItem>
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
