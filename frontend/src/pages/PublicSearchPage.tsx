import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import SearchCloseIcon from '../assets/cross-mark.svg';
import SearchIcon from '../assets/search.svg';
import { MainHeader, PublicWorkbook } from '../components';
import { DEVICE, STORAGE_KEY } from '../constants';
import { useRouter, useSnackbar } from '../hooks';
import { publicWorkbookState } from '../recoil';
import { Flex } from '../styles';
import { setSessionStorage } from '../utils';
import PageTemplate from './PageTemplate';

interface SearchBarStyleProps {
  isFocus: boolean;
  isSticky: boolean;
}

interface SearchButtonStyleProps {
  isFocus: boolean;
}

const PublicSearchPage = () => {
  const { data: publicWorkbooks, errorMessage } =
    useRecoilValue(publicWorkbookState);
  const updateWorkbooks = useResetRecoilState(publicWorkbookState);

  const [isFocus, setIsFocus] = useState(false);
  const [isSticky, setIsSticky] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState('');

  const { routePublicCards } = useRouter();
  const showSnackbar = useSnackbar();

  const stickyTriggerRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    updateWorkbooks();
  }, []);

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

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
  });

  useEffect(() => {
    if (!searchInputRef.current || !isFocus) return;

    searchInputRef.current.focus();
  }, [searchKeyword]);

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
          onBlur={() => setIsFocus(false)}
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
              <SearchCloseIcon width="0.5rem" height="0.5rem" />
            </KeywordResetButton>
          )}
          <SearchButton isFocus={isFocus}>
            <SearchIcon width="1.3rem" height="1.3rem" />
          </SearchButton>
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

const SearchButton = styled.button<SearchButtonStyleProps>`
  ${Flex({ items: 'center' })};

  ${({ theme, isFocus }) => css`
    & > svg {
      transition: all 0.3s ease;
      fill: ${isFocus ? theme.color.green : theme.color.gray_3};
    }
  `};
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
