import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import OpenIcon from '../assets/chevron-down-solid.svg';
import { Flex, scrollBarStyle } from '../styles';

interface OptionValues {
  id: number;
  name: string;
}

interface Props {
  optionValues: OptionValues[];
  setSelectedId: (id: number) => void;
}

interface CurrentStyleProps {
  isFocus: boolean;
}

const SelectBox = ({ optionValues, setSelectedId }: Props) => {
  const [isFocus, setIsFocus] = useState(false);
  const [currentId, setCurrentId] = useState(optionValues[0].id);

  return (
    <Container>
      <Current
        tabIndex={1}
        isFocus={isFocus}
        onClick={() => setIsFocus((prevState) => !prevState)}
        onBlur={() => setIsFocus(false)}
      >
        <ul>
          {optionValues.map(({ id, name }) => {
            return (
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
            );
          })}
        </ul>
        <StyledOpenIcon width="1.25rem" height="1.25rem" />
      </Current>
      <OptionList>
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
  width: 100%;
  margin: 0 auto;
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
  width: 100%;
  margin: 0;
  padding: 1rem;
`;

const HiddenInput = styled.input`
  display: none;

  &:checked + ${HiddenName} {
    display: block;
  }
`;

const OptionList = styled.ul`
  position: absolute;
  width: 100%;
  display: none;
  max-height: 10rem;
  overflow-y: auto;
  ${scrollBarStyle};

  ${({ theme }) => css`
    box-shadow: ${theme.boxShadow.button};
    border: 1px solid ${theme.color.gray_4};
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

  ${({ theme, isFocus }) => css`
    border: 1px solid ${theme.color.gray_4};
    box-shadow: ${theme.boxShadow.button};

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
