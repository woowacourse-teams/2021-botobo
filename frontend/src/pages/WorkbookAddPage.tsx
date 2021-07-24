import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import useModal from '../hooks/useModal';
import { Flex } from '../styles';

/**
 * TODO: 문제집 추가 헤더에 확인 버튼 추가
 * TODO: 공개 범위 셀렉트 눌렀을 때 바텀 모달 띄우기
 */

interface InputWrapperStyleProps {
  isFocus: boolean;
}

const WORKBOOK_NAME_MAXIMUM_LENGTH = 20;

const WorkbookAddPage = () => {
  const [inputValue, setInputValue] = useState('');
  const [isFocus, setIsFocus] = useState(false);
  const { openModal } = useModal();

  const onChangeInput: React.ChangeEventHandler<HTMLInputElement> = ({
    target,
  }) => {
    if (target.value.length > WORKBOOK_NAME_MAXIMUM_LENGTH) return;
    setInputValue(target.value);
  };

  return (
    <Container>
      <InputWrapper isFocus={isFocus}>
        <Input
          value={inputValue}
          onChange={onChangeInput}
          type="text"
          placeholder="문제집 이름"
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(false)}
        />
        <Limiter>
          {inputValue.length}/{WORKBOOK_NAME_MAXIMUM_LENGTH}
        </Limiter>
      </InputWrapper>
      <AccessLabel htmlFor="access-select">공개 범위</AccessLabel>
      <AccessSelectorWrapper onClick={() => openModal(<div>모달</div>)}>
        <AccessSelector id="access-select">전체 공개</AccessSelector>
      </AccessSelectorWrapper>
    </Container>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const InputWrapper = styled.div<InputWrapperStyleProps>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  height: 2rem;
  margin-bottom: 1.5rem;
  padding: 0 1rem;
  transition: border 0.1s linear;

  ${({ theme, isFocus }) => css`
    border-bottom: 1px solid
      ${isFocus ? theme.color.gray_8 : theme.color.gray_5};
  `};
`;

const Input = styled.input`
  width: 85%;
  outline: none;
  border: none;
  background-color: transparent;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

const Limiter = styled.span`
  width: 15%;
  text-align: center;

  ${({ theme }) => css`
    color: ${theme.color.gray_6};
  `}
`;

const AccessLabel = styled.label`
  margin-left: 1rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.small};
  `}
`;

const AccessSelectorWrapper = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  width: 100%;
  height: 2rem;
  margin-top: 0.3rem;
  padding: 0 1rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border: 1px solid ${theme.color.gray_5};
    border-radius: ${theme.borderRadius.square};
    font-size: ${theme.fontSize.default};
  `}

  &:after {
    content: '';
    width: 0.8rem;
    height: 0.5rem;
    clip-path: polygon(100% 0%, 0 0%, 50% 100%);

    ${({ theme }) => css`
      background-color: ${theme.color.gray_7};
    `}
  }
`;

const AccessSelector = styled.span`
  width: 100%;
  height: 100%;
  cursor: pointer;
  line-height: 2rem;
`;

export default WorkbookAddPage;
