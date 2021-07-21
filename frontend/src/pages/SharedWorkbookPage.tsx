import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';

import SearchCloseIcon from '../assets/cross-mark.svg';
import SearchIcon from '../assets/search.svg';
import { SharedWorkbookList } from '../components';
import { ROUTE } from '../constants';
import { Flex } from '../styles';

interface SearchStyleProps {
  isFocus: boolean;
}

const SharedWorkbookPage = () => {
  const [searchValue, setSearchValue] = useState('');
  const [isFocus, setIsFocus] = useState(false);
  const history = useHistory();

  return (
    <Container>
      <SearchBar isFocus={isFocus}>
        <SearchIcon width="1.3rem" height="1.3rem" />
        <SearchInput
          value={searchValue}
          onChange={({ target }) => setSearchValue(target.value)}
          placeholder="검색어를 입력해주세요"
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(false)}
        />
        {searchValue && (
          <button onClick={() => setSearchValue('')}>
            <SearchCloseIcon width="0.5rem" height="0.5rem" />
          </button>
        )}
      </SearchBar>
      <SharedWorkbookList
        onClickSharedWorkbook={(sharedWorkbookId) =>
          history.push(`${ROUTE.SHARED_CARDS.PATH}?id=${sharedWorkbookId}`)
        }
      />
    </Container>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1rem;
    `}
`;

const SearchBar = styled.div<SearchStyleProps>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  width: 100%;
  height: 3rem;
  margin-bottom: 1.5rem;
  padding: 0.5rem 1rem;
  transition: border 0.3s ease;

  ${({ theme, isFocus }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${isFocus ? theme.color.green : theme.color.gray_3};

    & > svg {
      transition: all 0.3s ease;
      fill: ${isFocus ? theme.color.green : theme.color.gray_3};
    }
  `};

  & > img {
    cursor: text;
  }
`;

const SearchInput = styled.input`
  width: 100%;
  height: 100%;
  outline: none;
  border: none;
  margin: 0 0.5rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

export default SharedWorkbookPage;