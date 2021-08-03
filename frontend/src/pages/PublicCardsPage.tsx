import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useSetRecoilState } from 'recoil';

import { getQuizzesAsync } from '../api';
import {
  Button,
  Checkbox,
  PageHeader,
  PublicCardsSelectBox,
  PublicQnACard,
} from '../components';
import { QUIZ_MODE, ROUTE } from '../constants';
import {
  useModal,
  usePublicCard,
  useRouter,
  useSnackbar,
  useWorkbook,
} from '../hooks';
import { quizModeState, quizState } from '../recoil';
import { Flex } from '../styles';

const PublicCardsPage = () => {
  const setQuiz = useSetRecoilState(quizState);
  const setQuizMode = useSetRecoilState(quizModeState);
  const { workbooks } = useWorkbook();
  const {
    workbookId,
    workbookName,
    cardCount,
    tags,
    publicCards,
    isAllCardChecked,
    checkAllCard,
    checkedCardCount,
    checkCard,
    takeCardsToMyWorkbook,
  } = usePublicCard();

  const showSnackbar = useSnackbar();
  const { openModal, closeModal } = useModal();
  const { routeQuiz } = useRouter();

  return (
    <>
      <PageHeader
        title={ROUTE.PUBLIC_CARDS.TITLE}
        sticky={true}
        rightContent={
          <StyledButton
            size="full"
            shape="square"
            backgroundColor="blue"
            onClick={async () => {
              const quizzes = await getQuizzesAsync(workbookId);

              setQuiz(quizzes);
              setQuizMode(QUIZ_MODE.OTHERS);
              routeQuiz();
            }}
          >
            바로 풀어보기
          </StyledButton>
        }
      />
      <Container>
        <WorkbookName>{workbookName}</WorkbookName>
        <CardCount>{cardCount}개의 카드</CardCount>
        <TagList>
          {tags.map(({ id, name }) => (
            <li key={id}>
              <Tag>
                <span>#</span>
                {name}
              </Tag>
            </li>
          ))}
        </TagList>
        <ul>
          {publicCards.map(({ id, question, answer, isChecked }) => (
            <CardItem key={id}>
              <PublicQnACard
                question={question}
                answer={answer}
                isChecked={isChecked}
                onClick={() => checkCard(id)}
              />
            </CardItem>
          ))}
        </ul>
        <BottomContent>
          <CheckboxWrapper>
            <Checkbox
              labelText="전체"
              name="checkAll"
              checked={isAllCardChecked}
              onChange={checkAllCard}
            />
          </CheckboxWrapper>
          <Button
            size="full"
            shape="rectangle"
            backgroundColor={checkedCardCount > 0 ? 'green' : 'gray_4'}
            disabled={checkedCardCount === 0}
            onClick={() => {
              if (checkedCardCount === 0) return;

              if (workbooks.length === 0) {
                showSnackbar({ message: '우선 문제집을 추가해주세요.' });

                return;
              }

              openModal({
                content: (
                  <PublicCardsSelectBox
                    workbooks={workbooks}
                    takeCardsToMyWorkbook={takeCardsToMyWorkbook}
                    closeModal={closeModal}
                  />
                ),
                closeIcon: 'down',
              });
            }}
          >
            <span>문제집으로 가져가기 ({checkedCardCount})</span>
          </Button>
        </BottomContent>
      </Container>
    </>
  );
};

const StyledButton = styled(Button)`
  width: 8rem;
`;

const Container = styled.div`
  margin-bottom: 3rem;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1.5rem;
    `}
`;

const WorkbookName = styled.h2`
  margin-bottom: 0.5rem;
`;

const CardCount = styled.div`
  margin-bottom: 0.5rem;

  ${({ theme }) => css`
    color: ${theme.color.gray_6};
  `};
`;

const TagList = styled.ul`
  ${Flex({ items: 'center' })};
`;

const Tag = styled.button`
  margin-right: 0.5rem;

  ${({ theme }) => css`
    color: ${theme.color.blue};
    font-size: ${theme.fontSize.default};
  `};

  & > span {
    margin-right: 0.1rem;
  }
`;

const CardItem = styled.li`
  margin-top: 1rem;

  &:not(:first-of-type) {
    margin-top: 2rem;
  }
`;

const BottomContent = styled.div`
  ${Flex()};
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
`;

const CheckboxWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 20%;
  min-width: 6rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `};
`;

export default PublicCardsPage;
