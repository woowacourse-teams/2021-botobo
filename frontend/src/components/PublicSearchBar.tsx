import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react';
import { useResetRecoilState } from 'recoil';

import { getTagKeywordAsync } from '../api';
import KeywordResetIcon from '../assets/cross-mark.svg';
import SearchIcon from '../assets/search.svg';
import { DEVICE } from '../constants';
import { useRouter, useSnackbar } from '../hooks';
import { publicSearchResultState } from '../recoil';
import { Flex, scrollBarStyle } from '../styles';
import { SearchKeywordResponse } from '../types';
import { isMobile } from '../utils';

interface SearchBarStyleProps {
  isFocus: boolean;
  isSticky: boolean;
  scaleX: number;
}

interface RecommendedKeywordStyleProps {
  isSelected: boolean;
}

const NOT_SELECTED = -1;

const PublicSearchBar = () => {
  const resetSearchResult = useResetRecoilState(publicSearchResultState);

  const [isFocus, setIsFocus] = useState(false);
  const [isSticky, setIsSticky] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [recommendedKeywords, setRecommendedKeywords] = useState<
    SearchKeywordResponse[]
  >([]);
  const [recommendedIndex, setRecommendedIndex] = useState(NOT_SELECTED);
  const [searchBarWidth, setSearchBarWidth] = useState(1);

  const { routePublicSearchResultQuery } = useRouter();
  const showSnackbar = useSnackbar();

  const stickyTriggerRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);
  const mountedRef = useRef(true);

  const searchForWorkbook = (keyword: string) => {
    mountedRef.current = false;
    resetSearchResult();
    routePublicSearchResultQuery({
      keyword,
      method: 'push',
    });
  };

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
  }, []);

  useEffect(() => {
    if (!searchInputRef.current || !isFocus) return;

    searchInputRef.current.focus();
  }, [searchKeyword]);

  useEffect(() => {
    if (!searchKeyword) {
      setRecommendedKeywords([]);

      return;
    }

    const getRecommendedKeywords = async () => {
      try {
        const tagKeywords = await getTagKeywordAsync(searchKeyword);
        if (!mountedRef.current) return;

        setRecommendedKeywords(tagKeywords);
      } catch (error) {
        console.error(error);
        showSnackbar({ message: '검색어를 불러오지 못했어요.' });
      }
    };

    getRecommendedKeywords();
  }, [searchKeyword]);

  useEffect(() => {
    const calculateSearchBarWidth = () => {
      if (!stickyTriggerRef.current || !isSticky) return;

      setSearchBarWidth(
        (stickyTriggerRef.current.offsetWidth + 40) /
          stickyTriggerRef.current.offsetWidth
      );
    };

    calculateSearchBarWidth();

    window.addEventListener('resize', calculateSearchBarWidth);

    return () => window.removeEventListener('resize', calculateSearchBarWidth);
  }, [isSticky]);

  const keyword =
    recommendedIndex === NOT_SELECTED
      ? searchKeyword
      : recommendedKeywords[recommendedIndex].name;

  return (
    <>
      <div ref={stickyTriggerRef} />
      <SearchBar
        name="search"
        role="search"
        isSticky={isSticky}
        isFocus={isFocus}
        scaleX={searchBarWidth}
        onFocus={() => setIsFocus(true)}
        onBlur={() => setIsFocus(isMobile ? true : false)}
        onSubmit={(event) => {
          event.preventDefault();
          if (!searchKeyword) return;

          searchForWorkbook(keyword);
        }}
      >
        <SearchInput
          value={keyword}
          onChange={({ target }) => {
            setRecommendedIndex(NOT_SELECTED);
            setSearchKeyword(target.value);
          }}
          onKeyDown={(event) => {
            if (recommendedKeywords.length === 0) return;

            if (event.key === 'ArrowDown') {
              setRecommendedIndex(
                (recommendedIndex + 1) % recommendedKeywords.length
              );
            }

            if (event.key === 'ArrowUp') {
              event.preventDefault();
              setRecommendedIndex(
                recommendedIndex === NOT_SELECTED
                  ? NOT_SELECTED
                  : recommendedIndex - 1
              );
            }
          }}
          placeholder={'문제집을 검색해보세요.'}
          ref={searchInputRef}
        />
        {searchKeyword && (
          <KeywordResetButton
            type="button"
            onClick={() => setSearchKeyword('')}
          >
            <KeywordResetIcon width="0.5rem" height="0.5rem" />
          </KeywordResetButton>
        )}
        <SearchButton isFocus={isFocus}>
          <SearchIcon width="1.3rem" height="1.3rem" />
        </SearchButton>
        {isFocus && searchKeyword && recommendedKeywords.length > 0 && (
          <Autocomplete isSticky={isSticky}>
            {recommendedKeywords.map(({ id, name }, index) => (
              <RecommendedKeyword
                key={id}
                onTouchStart={() => {
                  if (!isMobile) return;

                  searchInputRef.current?.blur();
                }}
                onMouseOver={() => setRecommendedIndex(index)}
                onMouseDown={() => searchForWorkbook(name)}
                isSelected={index === recommendedIndex}
              >
                <SearchIcon width="0.8rem" height="0.8rem" />
                {name}
              </RecommendedKeyword>
            ))}
            <button onClick={() => setIsFocus(false)}>닫기</button>
          </Autocomplete>
        )}
      </SearchBar>
    </>
  );
};

const SearchBar = styled.form<SearchBarStyleProps>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  position: relative;
  width: 100%;
  height: 3rem;
  margin-bottom: 1.5rem;
  padding: 0.5rem 1rem;
  transition: all 0.2s ease;
  z-index: 1;

  ${({ theme, isFocus, isSticky, scaleX }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${isFocus ? theme.color.green : theme.color.gray_3};

    ${isSticky &&
    css`
      position: sticky;
      top: -1px;
      border: none;
      border-bottom: 1px solid
        ${isFocus ? theme.color.green : theme.color.gray_3};
      transform: scaleX(${scaleX});

      & > input {
        transform: scaleX(${1 / scaleX}) translateX(-0.75rem);
      }

      & > button {
        transform: scaleX(${1 / scaleX});
      }

      & > ul {
        & > li {
          transform: scaleX(${1 / scaleX}) translateX(-1rem);
        }

        & > button {
          transform: scaleX(${1 / scaleX});
        }
      }

      @media ${DEVICE.TABLET} {
        border-left: 1px solid ${theme.color.gray_3};
        border-right: 1px solid ${theme.color.gray_3};
      }
    `}
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

const KeywordResetButton = styled.button`
  position: absolute;
  right: 3rem;
`;

const SearchButton = styled.button<Pick<SearchBarStyleProps, 'isFocus'>>`
  ${Flex({ items: 'center' })};

  ${({ theme, isFocus }) => css`
    & > svg {
      transition: all 0.3s ease;
      fill: ${isFocus ? theme.color.green : theme.color.gray_3};
    }
  `};
`;

const Autocomplete = styled.ul<Pick<SearchBarStyleProps, 'isSticky'>>`
  position: absolute;
  left: 0;
  width: 100%;
  max-height: calc(100vh - 3rem);
  padding: 0.5rem 0;
  overflow-y: auto;

  ${scrollBarStyle}

  ${({ theme, isSticky }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.card};
    border-bottom-right-radius: ${theme.borderRadius.square};
    border-bottom-left-radius: ${theme.borderRadius.square};
    border-top: 0;

    top: ${isSticky ? '3rem' : 'calc(3rem - 1px)'};

    & > button {
      float: right;
      color: ${theme.color.gray_1}
      font-size: ${theme.fontSize.small};
      margin: 0.5rem 0;
      margin-right: 1rem;
    }
  `};
`;

const RecommendedKeyword = styled.li<RecommendedKeywordStyleProps>`
  ${Flex({ items: 'center' })};
  cursor: pointer;
  padding: 0.7rem 1rem;

  & > svg {
    margin-right: 0.5rem;
  }

  ${({ theme, isSelected }) => css`
    background-color: ${isSelected && theme.color.gray_3};
  `}
`;

export default PublicSearchBar;
