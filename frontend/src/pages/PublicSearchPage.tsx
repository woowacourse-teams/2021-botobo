import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react';

import { getTagKeywordAsync, getUserKeywordAsync } from '../api';
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
import { SearchKeywordResponse } from '../types';
import { debounce } from '../utils';

const loadSrc = `${CLOUD_FRONT_DOMAIN}/frog.png`;

interface SearchBarStyleProps {
  isFocus: boolean;
  isSticky: boolean;
}

interface LoadImageStyleProps {
  isSearching: boolean;
  isFrogJumping: boolean;
}

const searchInfos = [
  {
    id: 1,
    name: '문제집',
    placeholder: '문제집을 검색해보세요',
    type: SEARCH_TYPE.NAME,
    searchForKeyword: null,
  },
  {
    id: 2,
    name: '태그',
    placeholder: '태그를 검색해보세요',
    type: SEARCH_TYPE.TAG,
    searchForKeyword: getTagKeywordAsync,
  },
  {
    id: 3,
    name: '작성자',
    placeholder: '작성자를 검색해보세요',
    type: SEARCH_TYPE.USER,
    searchForKeyword: getUserKeywordAsync,
  },
];

const PublicSearchPage = () => {
  const {
    workbookSearchResult,
    resetSearchResult,
    isSearching,
    isLoading,
    setIsSearching,
    searchForPublicWorkbook,
  } = usePublicSearch();
  const { keyword, type } = usePublicSearchQuery();

  const { routePublicSearchQuery } = useRouter();

  const stickyTriggerRef = useRef<HTMLDivElement>(null);

  const [isFocus, setIsFocus] = useState(false);
  const [isSticky, setIsSticky] = useState(false);
  const [inputValue, setInputValue] = useState(keyword);
  const [currentFocusTab, setCurrentFocusTab] = useState(
    searchInfos.find((item) => item.type === type) ?? searchInfos[0]
  );
  const [keywordSearchResult, setKeywordSearchResult] = useState<
    SearchKeywordResponse[]
  >([]);

  const [isFrogJumping, setIsFrogJumping] = useState(false);

  const searchForKeyword = async (keyword: string) => {
    if (keyword === '') {
      setIsSearching(false);

      return;
    }

    if (!currentFocusTab.searchForKeyword)
      return searchForPublicWorkbook({ keyword, start: 0 });

    try {
      const data = await currentFocusTab.searchForKeyword(keyword);

      setKeywordSearchResult(data);
      setIsSearching(false);
    } catch (error) {
      console.error(error);
      setIsSearching(false);
    }
  };

  const resetSearch = () => {
    resetSearchResult();
    setKeywordSearchResult([]);
  };

  useEffect(() => {
    routePublicSearchQuery({ keyword, type });
    searchForKeyword(keyword);
  }, []);

  useEffect(() => {
    if (!stickyTriggerRef.current) return;

    const searchTabObserver = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsSticky(false);

          return;
        }

        setIsSticky(true);
      },
      { threshold: [0, 1] }
    );

    searchTabObserver.observe(stickyTriggerRef.current);

    return () => searchTabObserver.disconnect();
  }, [stickyTriggerRef.current]);

  return (
    <>
      <MainHeader sticky={false} />
      <Container>
        <SearchTabWrapper>
          <SearchInfo>
            {searchInfos.map(({ id, name, type }) => (
              <SearchTabItem key={id} isFocus={currentFocusTab.id === id}>
                <button
                  onClick={() => {
                    const currentTab =
                      searchInfos.find((searchInfo) => searchInfo.id === id) ??
                      searchInfos[0];

                    setCurrentFocusTab(currentTab);
                    setInputValue('');
                    resetSearch();
                    routePublicSearchQuery({ type });
                  }}
                >
                  {name}
                </button>
              </SearchTabItem>
            ))}
          </SearchInfo>
          <SearchHr />
        </SearchTabWrapper>
        <SearchBarStickyTrigger ref={stickyTriggerRef} />
        <SearchBar isSticky={isSticky} isFocus={isFocus}>
          <SearchIcon width="1.3rem" height="1.3rem" />
          <SearchInput
            autoFocus={true}
            value={inputValue}
            onKeyDown={() => setIsFrogJumping(false)}
            onKeyUp={() => setIsFrogJumping(true)}
            onChange={({ target }) => {
              setIsSearching(true);
              setInputValue(target.value);
              resetSearch();
              debounce(() => {
                routePublicSearchQuery({
                  keyword: target.value,
                  type: currentFocusTab.type,
                });
                searchForKeyword(target.value);
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
                resetSearch();
              }}
            >
              <SearchCloseIcon width="0.5rem" height="0.5rem" />
            </button>
          )}
        </SearchBar>
        {currentFocusTab.type === 'name'
          ? workbookSearchResult.length > 0 && (
              <PublicWorkbookList
                isLoading={isLoading}
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
        <LoadImage isSearching={isSearching} isFrogJumping={isFrogJumping} />
      </Container>
    </>
  );
};

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

const SearchInfo = styled.ul`
  ${Flex()};
  column-gap: 2rem;
  margin-top: 0.5rem;
  height: 2rem;
`;

const SearchTabItem = styled.li<Pick<SearchBarStyleProps, 'isFocus'>>`
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

const SearchBarStickyTrigger = styled.div``;

const SearchBar = styled.div<SearchBarStyleProps>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  width: 100%;
  height: 3rem;
  margin-bottom: 1.5rem;
  padding: 0.5rem 1rem;
  transition: all 0.2s ease;

  ${({ theme, isFocus, isSticky }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${isFocus ? theme.color.green : theme.color.gray_3};

    ${isSticky &&
    css`
      position: sticky;
      z-index: 1;
      top: 0;
      transform: translateX(-1.25rem);
      width: calc(100% + 2.5rem);
      border: none;
      border-bottom: 1px solid
        ${isFocus ? theme.color.green : theme.color.gray_3};
    `}

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

const jumpAnimation = keyframes`
  0%{
    transform: translateY(0);
  }
  50%{
    transform:translateY(-100%);
  }
  100%{
    transform:translateY(0);
  }
`;

const LoadImage = styled.div<LoadImageStyleProps>`
  width: 3.75rem;
  height: 3.25rem;
  background-image: url(${loadSrc});
  background-repeat: no-repeat;
  background-size: contain;
  margin: 0 auto;
  margin-top: 6rem;

  ${({ isSearching, isFrogJumping }) => css`
    display: ${isSearching ? 'block' : 'none'};
    ${isFrogJumping &&
    css`
      animation: ${jumpAnimation} 1s infinite ease-in-out;
    `}
  `}
`;

export default PublicSearchPage;
