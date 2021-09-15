import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';
import { QuizResponse } from '../types';
import CardTemplate from './CardTemplate';

interface Props extends Omit<QuizResponse, 'id' | 'answer'> {
  isChecked: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const QuizResult = ({
  question,
  workbookName,
  encounterCount,
  isChecked,
  onClick,
}: Props) => (
  <CardTemplate isChecked={isChecked} onClick={onClick}>
    <TopContent>
      <WorkbookName>{workbookName}</WorkbookName>
      <EncounterCount>풀어본 횟수: {encounterCount}</EncounterCount>
    </TopContent>
    <Question>
      <span>Q.</span> {question}
    </Question>
  </CardTemplate>
);

const TopContent = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-bottom: 1rem;
`;

const WorkbookName = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.pink};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

const EncounterCount = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.gray_6};
    font-size: ${theme.fontSize.small};
  `}
`;

const Question = styled.div`
  ${Flex()};
  margin-bottom: 1rem;
  line-height: 1.5;

  & > span {
    margin-right: 0.3rem;
  }
`;

export default QuizResult;
