import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react';

import { getPublicWorkbookAsync, getTagKeywordAsync } from '../api';
import KeywordResetIcon from '../assets/cross-mark.svg';
import SearchIcon from '../assets/search.svg';
import { MainHeader, PublicWorkbook } from '../components';
import { DEVICE, STORAGE_KEY } from '../constants';
import { useErrorHandler, useRouter } from '../hooks';
import { Flex } from '../styles';
import { PublicWorkbookResponse, SearchKeywordResponse } from '../types';
import { isMobile, setSessionStorage } from '../utils';
import PageTemplate from './PageTemplate';
import { PublicSearchLoadable } from '.';

interface SearchBarStyleProps {
  isFocus: boolean;
  isSticky: boolean;
}

const PublicSearchPage = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [publicWorkbooks, setPublicWorkbooks] = useState<
    PublicWorkbookResponse[]
  >([]);

  const [isFocus, setIsFocus] = useState(false);
  const [isSticky, setIsSticky] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [recommendedKeywords, setRecommendedKeywords] = useState<
    SearchKeywordResponse[]
  >([]);

  const { routePublicCards, routePublicSearchResultQuery } = useRouter();
  const errorHandler = useErrorHandler();

  const stickyTriggerRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const getPublicWorkbooks = async () => {
      try {
        const newPublicWorkbooks = await getPublicWorkbookAsync();

        setPublicWorkbooks(newPublicWorkbooks);
        setIsLoading(false);
      } catch (error) {
        errorHandler(error);
        setIsLoading(false);
      }
    };

    getPublicWorkbooks();
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
  }, [isLoading]);

  useEffect(() => {
    if (!searchInputRef.current || !isFocus) return;

    searchInputRef.current.focus();
  }, [searchKeyword]);

  useEffect(() => {
    if (!searchKeyword) return;

    const getRecommendedKeywords = async () => {
      const tagKeywords = await getTagKeywordAsync(searchKeyword);

      setRecommendedKeywords(tagKeywords);
    };

    getRecommendedKeywords();
  }, [searchKeyword]);

  if (isLoading) return <PublicSearchLoadable />;

  return (
    <>
      <MainHeader sticky={false} />
      <StyledPageTemplate isScroll={true}>
        <div ref={stickyTriggerRef} />
        <SearchBar
          name="search"
          role="search"
          isSticky={isSticky}
          isFocus={isFocus}
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(isMobile ? true : false)}
          onSubmit={(event) => {
            event.preventDefault();
            if (!searchKeyword) return;

            routePublicSearchResultQuery({
              keyword: searchKeyword,
              method: 'push',
            });
          }}
        >
          <SearchInput
            value={searchKeyword}
            onChange={({ target }) => setSearchKeyword(target.value)}
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
              {recommendedKeywords.map(({ id, name }) => (
                <RecommendedKeyword
                  key={id}
                  onTouchStart={() => {
                    if (!isMobile) return;

                    searchInputRef.current?.blur();
                  }}
                  onMouseDown={() =>
                    routePublicSearchResultQuery({
                      keyword: name,
                      method: 'push',
                    })
                  }
                >
                  <SearchIcon width="0.8rem" height="0.8rem" />
                  {name}
                </RecommendedKeyword>
              ))}
              <button onClick={() => setIsFocus(false)}>닫기</button>
            </Autocomplete>
          )}
        </SearchBar>
        <StyledUl>
          {publicWorkbooks.map(({ id, name, cardCount, author }) => (
            <li key={id}>
              <PublicWorkbook
                name={name}
                cardCount={cardCount}
                author={author}
                onClick={() => {
                  setSessionStorage(STORAGE_KEY.PUBLIC_WORKBOOK_ID, id);
                  routePublicCards();
                }}
              />
            </li>
          ))}
        </StyledUl>
      </StyledPageTemplate>
    </>
  );
};

const StyledPageTemplate = styled(PageTemplate)`
  padding-top: 1rem;
`;

const SearchBar = styled.form<SearchBarStyleProps>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  position: relative;
  width: 100%;
  height: 3rem;
  margin-bottom: 1.5rem;
  padding: 0.5rem 1rem;
  transition: all 0.2s ease;
  z-index: 1;

  ${({ theme, isFocus, isSticky }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${isFocus ? theme.color.green : theme.color.gray_3};

    ${isSticky &&
    css`
      position: sticky;
      top: 0;
      transform: translateX(-1.25rem);
      width: calc(100% + 2.5rem);
      border: none;
      border-bottom: 1px solid
        ${isFocus ? theme.color.green : theme.color.gray_3};

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
  padding: 0.5rem 0;

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

const RecommendedKeyword = styled.li`
  ${Flex({ items: 'center' })};
  cursor: pointer;
  padding: 0.7rem 1rem;

  & > svg {
    margin-right: 0.5rem;
  }

  &:hover {
    ${({ theme }) => css`
      background-color: ${theme.color.gray_3};
    `}
  }
`;

const StyledUl = styled.ul`
  position: relative;
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;

  & > li:last-of-type {
    margin-bottom: 1rem;
  }
`;

export default PublicSearchPage;
