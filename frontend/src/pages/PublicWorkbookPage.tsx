import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';
import { useRecoilState, useSetRecoilState } from 'recoil';

import { getPublicWorkbookAsync } from '../api';
import SearchCloseIcon from '../assets/cross-mark.svg';
import SearchIcon from '../assets/search.svg';
import { PublicWorkbookList } from '../components';
import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { publicWorkbookIdState, searchKeywordState } from '../recoil';
import { Flex } from '../styles';
import { PublicWorkbookResponse } from '../types';
import { debounce, setSessionStorage } from '../utils';

interface SearchStyleProps {
  isFocus: boolean;
}

const PublicWorkbookPage = () => {
  const [keyword, setKeyword] = useRecoilState(searchKeywordState);
  const [publicWorkbooks, setPublicWorkbooks] = useState<
    PublicWorkbookResponse[]
  >([]);
  const setPublicWorkbookId = useSetRecoilState(publicWorkbookIdState);
  const [inputValue, setInputValue] = useState(keyword);
  const [isFocus, setIsFocus] = useState(false);
  const { routePublicCards } = useRouter();

  const search = async (value: string) => {
    try {
      const data = await getPublicWorkbookAsync(value);
      setPublicWorkbooks(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (inputValue) return;

    setPublicWorkbooks([]);
  }, [inputValue]);

  return (
    <Container>
      <SearchBar isFocus={isFocus}>
        <SearchIcon width="1.3rem" height="1.3rem" />
        <SearchInput
          autoFocus={true}
          value={inputValue}
          onChange={({ target }) => {
            setInputValue(target.value);
            debounce(() => search(target.value), 200);
          }}
          placeholder="검색어를 입력해주세요"
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(false)}
        />
        {inputValue && (
          <button onClick={() => setInputValue('')}>
            <SearchCloseIcon width="0.5rem" height="0.5rem" />
          </button>
        )}
      </SearchBar>
      <PublicWorkbookList
        publicWorkbooks={publicWorkbooks}
        onClickPublicWorkbook={async (id) => {
          await setPublicWorkbookId(id);
          setSessionStorage(STORAGE_KEY.PUBLIC_WORKBOOK_ID, id);
          setKeyword(inputValue);
          routePublicCards(id);
        }}
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

export default PublicWorkbookPage;
