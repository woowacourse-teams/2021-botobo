import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { QuizResponse } from '../types';
import CardTemplate from './CardTemplate';

interface Props extends Omit<QuizResponse, 'id' | 'answer' | 'encounterCount'> {
  isChecked: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const QuizResult = ({ question, workbookName, isChecked, onClick }: Props) => (
  <CardTemplate isChecked={isChecked} onClick={onClick}>
    <WorkbookName>{workbookName}</WorkbookName>
    <Question>Q. {question}</Question>
  </CardTemplate>
);

const WorkbookName = styled.div`
  margin-bottom: 1rem;

  ${({ theme }) => css`
    color: ${theme.color.pink};
    font-size: ${theme.fontSize.small};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

const Question = styled.div`
  margin-bottom: 1rem;
`;

export default QuizResult;
