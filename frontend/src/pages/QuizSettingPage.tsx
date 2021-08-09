import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import {
  Button,
  Checkbox,
  MainHeader,
  SelectBox,
  WorkbookList,
} from '../components';
import { ROUTE } from '../constants';
import { useQuizSetting } from '../hooks';

const counts = [10, 15, 20, 25, 30].map((count, index) => ({
  id: index,
  name: count,
}));

const QuizSettingPage = () => {
  const { workbooks, checkWorkbook, startQuiz } = useQuizSetting();
  const [selectedCountId, setSelectedCountId] = useState(counts[0].id);
  const [isTimeCheck, setIsTimeCheck] = useState(false);

  return (
    <>
      <MainHeader />
      <Container>
        <Title>{ROUTE.QUIZ_SETTING.TITLE}</Title>
        <span>어떤 문제를 풀어볼까요?</span>
        <WorkbookWrapper>
          <WorkbookList workbooks={workbooks} onClickWorkbook={checkWorkbook} />
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
          checked={isTimeCheck}
          onChange={({ target }) => setIsTimeCheck(target.checked)}
        />
        <StyleButton onClick={startQuiz} size="full">
          시작!
        </StyleButton>
      </Container>
    </>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

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

const StyleButton = styled(Button)`
  margin-top: 3.5rem;
`;

export default QuizSettingPage;
