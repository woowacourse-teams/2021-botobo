import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import SearchCloseIcon from '../assets/cross-mark.svg';
import SearchIcon from '../assets/search.svg';
import { PublicWorkbookList } from '../components';
import { CLOUD_FRONT_DOMAIN, STORAGE_KEY } from '../constants';
import { usePublicWorkbook, useRouter } from '../hooks';
import { Flex } from '../styles';
import { debounce, setSessionStorage } from '../utils';

const loadSrc = `${CLOUD_FRONT_DOMAIN}/frog.png`;

interface SearchStyleProps {
  isFocus: boolean;
}

interface LoadImageWrapperStyleProps {
  isLoading: boolean;
}

const PublicWorkbookPage = () => {
  const {
    inputValue,
    setInputValue,
    publicWorkbooks,
    setPublicWorkbooks,
    setPublicWorkbookId,
    setKeyword,
    search,
    isLoading,
    setIsLoading,
  } = usePublicWorkbook();
  const [isFocus, setIsFocus] = useState(false);
  const { routePublicCards } = useRouter();

  return (
    <Container>
      <SearchBar isFocus={isFocus}>
        <SearchIcon width="1.3rem" height="1.3rem" />
        <SearchInput
          autoFocus={true}
          value={inputValue}
          onChange={({ target }) => {
            setInputValue(target.value);
            setIsLoading(true);
            setPublicWorkbooks([]);
            debounce(() => search(target.value), 200);
          }}
          placeholder="검색어를 입력해주세요"
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(false)}
        />
        {inputValue && (
          <button
            onClick={() => {
              setInputValue('');
              setPublicWorkbooks([]);
            }}
          >
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
          routePublicCards();
        }}
      />
      <LoadImageWrapper isLoading={isLoading}>
        <LoadImage />
      </LoadImageWrapper>
    </Container>
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

const LoadImageWrapper = styled.div<LoadImageWrapperStyleProps>`
  ${Flex({ justify: 'center', items: 'center' })};
  z-index: -1;
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;

  ${({ isLoading }) => css`
    display: ${isLoading ? 'flex' : 'none'};
  `}
`;

const LoadImage = styled.div`
  width: 3.75rem;
  height: 3.25rem;
  background-image: url(${loadSrc});
  background-repeat: no-repeat;
  background-size: contain;
  animation: ${jumpAnimation} 1s infinite linear;
`;

export default PublicWorkbookPage;
