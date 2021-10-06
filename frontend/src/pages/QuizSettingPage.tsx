import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import {
  Button,
  CardTemplate,
  Checkbox,
  MainHeader,
  SelectBox,
} from '../components';
import { ROUTE } from '../constants';
import { useQuizSetting } from '../hooks';
import { WorkbookListStyle } from '../styles';
import PageTemplate from './PageTemplate';

const counts = [10, 15, 20, 25, 30].map((count, index) => ({
  id: index,
  name: count,
}));

const QuizSettingPage = () => {
  const { workbooks, checkWorkbook, startQuiz } = useQuizSetting();
  const [selectedCountId, setSelectedCountId] = useState(counts[0].id);
  const [isTimeChecked, setIsTimeChecked] = useState(false);

  return (
    <>
      <MainHeader />
      <PageTemplate isScroll={true}>
        <Title>{ROUTE.QUIZ_SETTING.TITLE}</Title>
        <span>어떤 문제를 풀어볼까요?</span>
        <WorkbookWrapper>
          <StyledUl>
            {workbooks.map(({ id, name, cardCount, isChecked }) => (
              <li key={id}>
                <CardTemplate
                  isChecked={isChecked}
                  onClick={() => checkWorkbook(id)}
                >
                  <Name>{name}</Name>
                  <CardCount>{cardCount}개의 카드</CardCount>
                </CardTemplate>
              </li>
            ))}
          </StyledUl>
        </WorkbookWrapper>
        <span>몇 문제를 풀어볼까요?</span>
        <SelectBoxWrapper>
          <SelectBox
            optionValues={counts}
            listHeight="8rem"
            setSelectedId={(id) => setSelectedCountId(id)}
          />
        </SelectBoxWrapper>
        <Checkbox
          name="time-check"
          labelText="시간도 확인하고 싶어요"
          checked={isTimeChecked}
          onChange={({ target }) => setIsTimeChecked(target.checked)}
        />
        <StyleButton
          onClick={() =>
            startQuiz({
              count:
                counts.find((count) => count.id === selectedCountId)?.name ??
                counts[0].name,
              isTimeChecked,
            })
          }
          size="full"
        >
          시작!
        </StyleButton>
      </PageTemplate>
    </>
  );
};

const Title = styled.h2`
  margin-bottom: 1rem;
`;

const WorkbookWrapper = styled.div`
  margin-top: 1rem;
  margin-bottom: 2.5rem;
`;

const SelectBoxWrapper = styled.div`
  margin-top: 1rem;
  margin-bottom: 3.5rem;
`;

const StyledUl = styled.ul`
  ${WorkbookListStyle};
  margin: 1rem 0;
`;

const StyleButton = styled(Button)`
  margin-top: 3.5rem;
`;

const Name = styled.div`
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin: 0.3rem 0;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

const CardCount = styled.div`
  margin-bottom: 1rem;

  ${({ theme }) => css`
    color: ${theme.color.gray_6};
    font-size: ${theme.fontSize.small};
  `};
`;

export default QuizSettingPage;
