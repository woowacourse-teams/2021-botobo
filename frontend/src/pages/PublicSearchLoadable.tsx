import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import SearchIcon from '../assets/search.svg';
import { CardSkeleton, HeaderSkeleton } from '../components';
import { DEVICE } from '../constants';
import { Flex } from '../styles';
import PageTemplate from './PageTemplate';

const PublicSearchResultLoadable = () => (
  <>
    <HeaderSkeleton />
    <StyledPageTemplate isScroll={true}>
      <SearchBar name="search" role="search">
        <SearchInput />
        <SearchButton>
          <SearchIcon width="1.3rem" height="1.3rem" />
        </SearchButton>
      </SearchBar>
      <StyledUl>
        {[...Array(12)].map((_, index) => (
          <StyledCardSkeleton key={index} />
        ))}
      </StyledUl>
    </StyledPageTemplate>
  </>
);

const StyledPageTemplate = styled(PageTemplate)`
  padding-top: 1rem;
`;

const SearchBar = styled.form`
  ${Flex({ justify: 'space-between', items: 'center' })};
  position: relative;
  width: 100%;
  height: 3rem;
  margin-bottom: 1.5rem;
  padding: 0.5rem 1rem;
  transition: all 0.2s ease;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${theme.color.gray_3};
  `};
`;

const SearchInput = styled.input`
  width: 80%;
  height: 100%;
  outline: none;
  border: none;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

const SearchButton = styled.button`
  ${Flex({ items: 'center' })};

  ${({ theme }) => css`
    & > svg {
      transition: all 0.3s ease;
      fill: ${theme.color.gray_3};
    }
  `};
`;

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;
  margin: 1rem 0;

  @media ${DEVICE.TABLET} {
    grid-template-columns: repeat(2, 1fr);
  }
`;

const StyledCardSkeleton = styled(CardSkeleton)`
  ${Flex({ direction: 'column', items: 'flex-start' })};
  height: 8.75rem;
`;

export default PublicSearchResultLoadable;
