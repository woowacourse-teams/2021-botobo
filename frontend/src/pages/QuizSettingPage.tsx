import styled from '@emotion/styled';
import React, { useState } from 'react';

import {
  Button,
  Checkbox,
  MainHeader,
  SelectBox,
  Workbook,
} from '../components';
import { DEVICE, ROUTE } from '../constants';
import { useQuizSetting } from '../hooks';
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
                <Workbook
                  name={name}
                  cardCount={cardCount}
                  isChecked={isChecked}
                  onClick={() => checkWorkbook(id)}
                />
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
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;
  margin: 1rem 0;

  @media ${DEVICE.TABLET} {
    grid-template-columns: repeat(2, 1fr);
  }
`;

const StyleButton = styled(Button)`
  margin-top: 3.5rem;
`;

export default QuizSettingPage;
