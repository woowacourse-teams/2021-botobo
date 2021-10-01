import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import SearchIcon from '../assets/search.svg';
import { CardSkeletonList, HeaderSkeleton } from '../components';
import { Flex, loadContent } from '../styles';
import PageTemplate from './PageTemplate';

const PublicSearchLoadable = () => (
  <>
    <HeaderSkeleton />
    <StyledPageTemplate isScroll={true}>
      <SearchBar name="search" role="search">
        <SearchInput />
        <SearchButton>
          <SearchIcon width="1.3rem" height="1.3rem" />
        </SearchButton>
      </SearchBar>
      <Title />
      <Description />
      <CardSkeletonList count={12} hasAuthor={true} hasTag={true} />
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

const Title = styled.div`
  height: 2rem;
  width: 20%;
  margin-bottom: 1rem;

  ${loadContent}
`;

const Description = styled.div`
  height: 1rem;
  width: 30%;

  ${loadContent}
`;

export default PublicSearchLoadable;
