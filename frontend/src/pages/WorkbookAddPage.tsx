import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { InputField, PageHeader } from '../components';
import { ROUTE, WORKBOOK_NAME_MAXIMUM_LENGTH } from '../constants';
import { FormProvider } from '../contexts';
import useModal from '../hooks/useModal';
import { Flex } from '../styles';

const validateWorkbookName = (value: string) => {
  if (value.length > WORKBOOK_NAME_MAXIMUM_LENGTH) {
    throw new Error(
      `문제집 이름은 ${WORKBOOK_NAME_MAXIMUM_LENGTH}자 이하여야 합니다.`
    );
  }
};

const WorkbookAddPage = () => {
  const { openModal } = useModal();

  return (
    <FormProvider
      initialValues={{ name: '' }}
      validators={{ name: validateWorkbookName }}
      onSubmit={() => {
        console.log('hi');
      }}
    >
      <PageHeader
        title={ROUTE.WORKBOOK_ADD.TITLE}
        rightContent={<SubmitButton>확인</SubmitButton>}
      />
      <Container>
        <Input
          name="name"
          placeholder="문제집 이름"
          focusColor="gray"
          maxLength={WORKBOOK_NAME_MAXIMUM_LENGTH}
        />
        <AccessLabel htmlFor="access-select">공개 범위</AccessLabel>
        <AccessSelectorWrapper onClick={() => openModal(<div>모달</div>)}>
          <AccessSelector id="access-select">전체 공개</AccessSelector>
        </AccessSelectorWrapper>
      </Container>
    </FormProvider>
  );
};

const SubmitButton = styled.button`
  margin-right: 1rem;
`;

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const Input = styled(InputField)`
  width: 100%;
  padding: 0.5rem 1rem;
  transition: border 0.2s linear;
  outline: none;
  border: none;
  background-color: transparent;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
    border-bottom: 1px solid ${theme.color.gray_5};
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

  &::after {
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
