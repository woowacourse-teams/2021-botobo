import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';

import OpenIcon from '../assets/chevron-down-solid.svg';
import { Flex, scrollBarStyle } from '../styles';

interface OptionValue {
  id: number;
  name: string | number;
}

interface Props {
  title?: string;
  optionValues: OptionValue[];
  setSelectedId: (id: number) => void;
  listHeight?: string;
  disabled?: boolean;
}

interface CurrentStyleProps {
  isFocus: boolean;
  disabled: boolean;
}

interface OptionListStyleProps {
  listHeight: string;
}

const SelectBox = ({
  title,
  optionValues,
  setSelectedId,
  listHeight = '10rem',
  disabled = false,
}: Props) => {
  const [isFocus, setIsFocus] = useState(false);
  const [currentId, setCurrentId] = useState(optionValues[0].id);

  useEffect(() => {
    setCurrentId(optionValues[0].id);
  }, [disabled]);

  return (
    <Container tabIndex={1} onBlur={() => setIsFocus(false)}>
      {title && <Title>{title}</Title>}
      <Current
        onClick={() => {
          if (disabled) return;

          setIsFocus((prevState) => !prevState);
        }}
        isFocus={isFocus}
        disabled={disabled}
      >
        <ul>
          {optionValues.map(({ id, name }) => (
            <HiddenWrapper key={id}>
              <HiddenInput
                type="radio"
                id={`radio-${id}`}
                checked={id === currentId}
                onChange={() => {
                  setSelectedId(id);
                  setCurrentId(id);
                }}
                name="select"
              />
              <HiddenName>{name}</HiddenName>
            </HiddenWrapper>
          ))}
        </ul>
        <StyledOpenIcon width="1.25rem" height="1.25rem" />
      </Current>
      <OptionList listHeight={listHeight}>
        {optionValues.map(({ id, name }) => (
          <li key={id}>
            <Name htmlFor={`radio-${id}`}>{name}</Name>
          </li>
        ))}
      </OptionList>
    </Container>
  );
};

const Container = styled.div`
  position: relative;
  z-index: 1;
  width: 100%;
  margin: 0 auto;
`;

const Title = styled.div`
  margin-bottom: 0.5rem;
  line-height: 1.5;
`;

const HiddenWrapper = styled.li`
  ${Flex()};
`;

const StyledOpenIcon = styled(OpenIcon)`
  position: absolute;
  top: 50%;
  right: 1rem;
  transform: translateY(-50%);
  opacity: 0.3;
  transition: transform 0.2s ease;
`;

const HiddenName = styled.div`
  display: none;
  width: calc(100% - 2rem);
  margin: 0;
  padding: 1rem;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

const HiddenInput = styled.input`
  display: none;

  &:checked + ${HiddenName} {
    display: block;
  }
`;

const OptionList = styled.ul<OptionListStyleProps>`
  position: absolute;
  width: 100%;
  display: none;
  overflow-y: auto;
  ${scrollBarStyle};

  ${({ theme, listHeight }) => css`
    box-shadow: ${theme.boxShadow.button};
    border: 1px solid ${theme.color.gray_4};

    max-height: ${listHeight};

    & > li {
      background-color: ${theme.color.white};
    }
  `}
`;

const Name = styled.label`
  display: block;
  padding: 1rem;

  &:hover,
  &:focus {
    ${({ theme }) => css`
      background-color: ${theme.color.gray_1};
    `}
  }
`;

const Current = styled.div<CurrentStyleProps>`
  position: relative;
  cursor: pointer;
  outline: none;

  ${({ theme, isFocus, disabled }) => css`
    border: 1px solid ${theme.color.gray_4};
    box-shadow: ${theme.boxShadow.button};
    background-color: ${disabled ? theme.color.gray_3 : ''};

    ${isFocus &&
    css`
      & + ${OptionList} {
        display: block;
        border-top: none;

        & ${Name} {
          cursor: pointer;
        }
      }

      & ${HiddenName} {
        background-color: ${theme.color.gray_1};
      }

      & ${StyledOpenIcon} {
        transform: translateY(-50%) rotate(180deg);
      }
    `}
  `}
`;

export default SelectBox;
