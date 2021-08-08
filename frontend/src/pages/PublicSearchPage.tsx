import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';

import SearchCloseIcon from '../assets/cross-mark.svg';
import SearchIcon from '../assets/search.svg';
import {
  MainHeader,
  PublicSearchList,
  PublicWorkbookList,
} from '../components';
import { CLOUD_FRONT_DOMAIN, SEARCH_TYPE } from '../constants';
import { usePublicSearch, usePublicSearchQuery, useRouter } from '../hooks';
import { Flex } from '../styles';
import { debounce } from '../utils';

const loadSrc = `${CLOUD_FRONT_DOMAIN}/frog.png`;

interface Focus {
  isFocus: boolean;
}

interface LoadImageStyleProps {
  isLoading: boolean;
}

type SearchTabList = {
  id: number;
  name: string;
  placeholder: string;
};

const searchTabList = [
  {
    id: 1,
    name: '문제집',
    placeholder: '문제집을 검색해보세요',
    type: SEARCH_TYPE.NAME,
  },
  {
    id: 2,
    name: '태그',
    placeholder: '태그를 검색해보세요',
    type: SEARCH_TYPE.TAG,
  },
  {
    id: 3,
    name: '작성자',
    placeholder: '작성자를 검색해보세요',
    type: SEARCH_TYPE.USER,
  },
];

const PublicSearchPage = () => {
  const {
    keywordSearchResult,
    workbookSearchResult,
    resetSearchResult,
    isLoading,
    setIsLoading,
    searchForPublicWorkbook,
    searchForKeyword,
  } = usePublicSearch();
  const { keyword, type } = usePublicSearchQuery();

  const { routePublicSearchQuery } = useRouter();

  const [isFocus, setIsFocus] = useState(false);
  const [inputValue, setInputValue] = useState(keyword);

  const [currentFocusTab, setCurrentFocusTab] = useState(
    searchTabList.find((item) => item.type === type) ?? searchTabList[0]
  );

  useEffect(() => {
    routePublicSearchQuery({ keyword, type });
    searchForKeyword(keyword, type);
  }, []);

  return (
    <>
      <MainHeader />
      <Container>
        <SearchTabWrapper>
          <SearchTabList>
            {searchTabList.map(({ id, name, type }) => (
              <SearchTabItem key={id} isFocus={currentFocusTab.id === id}>
                <button
                  onClick={() => {
                    const currentTab =
                      searchTabList.find((item) => item.id === id) ??
                      searchTabList[0];

                    setCurrentFocusTab(currentTab);
                    setInputValue('');
                    resetSearchResult();
                    routePublicSearchQuery({ type });
                  }}
                >
                  {name}
                </button>
              </SearchTabItem>
            ))}
          </SearchTabList>
          <SearchHr />
        </SearchTabWrapper>
        <SearchBar isFocus={isFocus}>
          <SearchIcon width="1.3rem" height="1.3rem" />
          <SearchInput
            autoFocus={true}
            value={inputValue}
            onChange={({ target }) => {
              setInputValue(target.value);
              setIsLoading(true);
              resetSearchResult();
              debounce(() => {
                routePublicSearchQuery({
                  keyword: target.value,
                  type: currentFocusTab.type,
                });
                searchForKeyword(target.value, currentFocusTab.type);
              }, 400);
            }}
            placeholder={currentFocusTab.placeholder}
            onFocus={() => setIsFocus(true)}
            onBlur={() => setIsFocus(false)}
          />
          {inputValue && (
            <button
              onClick={() => {
                setInputValue('');
                resetSearchResult();
              }}
            >
              <SearchCloseIcon width="0.5rem" height="0.5rem" />
            </button>
          )}
        </SearchBar>
        {currentFocusTab.type === 'name'
          ? workbookSearchResult.length > 0 && (
              <PublicWorkbookList
                publicWorkbooks={workbookSearchResult}
                searchForPublicWorkbook={searchForPublicWorkbook}
              />
            )
          : keywordSearchResult.length > 0 && (
              <PublicSearchList
                searchItems={keywordSearchResult}
                type={currentFocusTab.type}
              />
            )}
        <LoadImage isLoading={isLoading} />
      </Container>
    </>
  );
};

const jumpAnimation = keyframes`
  0%{
    transform: translateY(100%);
  }
  50%{
    transform:translateY(0);
  }
  100%{
    transform:translateY(100%);
  }
`;

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1rem;
    `}
`;

const SearchTabWrapper = styled.nav`
  margin-bottom: 1rem;
`;

const SearchTabList = styled.ul`
  ${Flex()};
  column-gap: 2rem;
  margin-top: 0.5rem;
  height: 2rem;
`;

const SearchTabItem = styled.li<Focus>`
  ${({ theme, isFocus }) => css`
    ${isFocus
      ? css`
          color: ${theme.color.gray_9};
          border-bottom: 2px solid ${theme.color.gray_9};
          font-weight: ${theme.fontWeight.bold};
        `
      : css`
          color: ${theme.color.gray_5};
        `}
  `}

  & > button {
    color: inherit;
    font-weight: inherit;
  }
`;

const SearchHr = styled.hr`
  position: absolute;
  width: 100%;
  height: 1px;
  left: 0;
  border: none;

  ${({ theme }) => css`
    background-color: ${theme.color.gray_4};
  `}
`;

const SearchBar = styled.div<Focus>`
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

const LoadImage = styled.div<LoadImageStyleProps>`
  width: 3.75rem;
  height: 3.25rem;
  background-image: url(${loadSrc});
  background-repeat: no-repeat;
  background-size: contain;
  animation: ${jumpAnimation} 1s infinite ease-in-out;
  margin: 0 auto;
  margin-top: 1rem;

  ${({ isLoading }) => css`
    display: ${isLoading ? 'block' : 'none'};
  `}
`;

export default PublicSearchPage;
