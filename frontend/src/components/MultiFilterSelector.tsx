import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';
import { useSetRecoilState } from 'recoil';

import { PublicWorkbookAsync } from '../api';
import SearchCloseIcon from '../assets/cross-mark.svg';
import CheckIcon from '../assets/tick.svg';
import { useModal } from '../hooks';
import { UsePublicSearchQueryType } from '../hooks/usePublicSearchQuery';
import { publicSearchResultState } from '../recoil';
import { Flex, scrollBarStyle } from '../styles';
import {
  MultiFilterNames,
  MultiFilterTypes,
  MultiFilterValue,
} from '../types/filter';

interface Props {
  type: MultiFilterTypes;
  name: MultiFilterNames;
  values: MultiFilterValue[];
  query: UsePublicSearchQueryType;
  setFilteredPublicWorkbook: (newQuery: PublicWorkbookAsync) => void;
}

interface SearchBarStyleProps {
  isFocus: boolean;
}

interface MultiFilterItemStyleProps {
  isSelected: boolean;
}

const MultiFilterSelector = ({
  type,
  name,
  values: data,
  query,
  setFilteredPublicWorkbook,
}: Props) => {
  const { closeModal } = useModal();

  const [filterKeyword, setFilterKeyword] = useState('');
  const [isSearchBarFocus, setIsSearchBarFocus] = useState(false);
  const [values, setValues] = useState(data);

  const setPublicWorkbookState = useSetRecoilState(publicSearchResultState);

  const regExpKeyword = new RegExp(filterKeyword, 'i');

  const checkFilterItem = (name: string) => {
    setValues((prevValue) =>
      prevValue.map((item) => {
        if (item.name !== name) return item;

        return { ...item, isSelected: !item.isSelected };
      })
    );
  };

  const setSelectedValues = () => {
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      multiFilters: prevValue.multiFilters.map((item) => {
        if (item.type !== type) return item;

        return { ...item, values };
      }),
    }));
  };

  return (
    <>
      <Title>{name} 선택</Title>
      <SearchBar isFocus={isSearchBarFocus}>
        <SearchInput
          onFocus={() => setIsSearchBarFocus(true)}
          onBlur={() => setIsSearchBarFocus(false)}
          value={filterKeyword}
          onChange={({ target }) => setFilterKeyword(target.value)}
          name="search"
          role="search"
          placeholder={`${name}를 입력해보세요.`}
        />
        {filterKeyword && (
          <button type="button" onClick={() => setFilterKeyword('')}>
            <SearchCloseIcon width="0.5rem" height="0.5rem" />
          </button>
        )}
      </SearchBar>
      <MultiFilterList>
        {values
          .filter((v) => regExpKeyword.test(v.name.replaceAll(/\s/g, '')))
          .map(({ id, name, isSelected }) => (
            <MultiFilterItem
              key={id}
              isSelected={isSelected}
              onClick={() => checkFilterItem(name)}
            >
              <div>{name}</div>
              {isSelected && <CheckIcon width="1rem" height="1rem" />}
            </MultiFilterItem>
          ))}
      </MultiFilterList>
      <Confirm
        type="button"
        onClick={() => {
          setSelectedValues();
          closeModal();
          setFilteredPublicWorkbook({
            ...query,
            [type]: values
              .filter(({ isSelected }) => isSelected)
              .map(({ id }) => id)
              .join(','),
          });
        }}
      >
        확인
      </Confirm>
    </>
  );
};

const Title = styled.h3`
  text-align: center;
  margin-top: 1rem;
  margin-bottom: 2rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
    font-weight: ${theme.fontWeight.semiBold};
  `}
`;

const SearchBar = styled.div<SearchBarStyleProps>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  position: relative;
  width: 100%;
  height: 3rem;
  margin-bottom: 1.5rem;
  padding: 0.5rem 1rem;
  transition: all 0.2s ease;

  ${({ theme, isFocus }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${isFocus ? theme.color.green : theme.color.gray_3};
  `};
`;

const SearchInput = styled.input`
  width: 100%;
  height: 100%;
  outline: none;
  border: none;
  margin-right: 1rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

const MultiFilterList = styled.ul`
  height: 30vh;
  overflow-y: auto;
  word-break: break-all;
  margin-bottom: 3.5rem;

  ${scrollBarStyle}
`;

const MultiFilterItem = styled.li<MultiFilterItemStyleProps>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  padding: 1rem 0;
  cursor: pointer;
  line-height: 1.5;

  ${({ theme, isSelected }) => css`
    ${isSelected &&
    css`
      color: ${theme.color.green};
    `}

    & > div {
      width: 80%;

      &:hover {
        opacity: 0.6;
      }
    }

    & > svg {
      margin-right: 1rem;
      fill: ${theme.color.green};
    }
  `}
`;

const Confirm = styled.button`
  position: absolute;
  bottom: 0;
  left: 0;
  padding: 1rem 0;
  text-align: center;
  width: 100%;

  ${({ theme }) => css`
    border-top: 1px solid ${theme.color.gray_5};
  `}
`;

export default MultiFilterSelector;
