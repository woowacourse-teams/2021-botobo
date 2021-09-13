import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { getQuizzesAsync } from '../api';
import EmptyHeartIcon from '../assets/heart-regular.svg';
import FillHeartIcon from '../assets/heart-solid.svg';
import {
  Button,
  Checkbox,
  PageHeader,
  PublicCardsSelectBox,
  PublicQnACard,
} from '../components';
import { QUIZ_MODE, ROUTE, theme } from '../constants';
import { useModal, usePublicCard, useRouter, useWorkbook } from '../hooks';
import { quizModeState, quizState, userState } from '../recoil';
import { Flex } from '../styles';
import { debounce } from '../utils';
import PageTemplate from './PageTemplate';
import PublicCardsLoadable from './PublicCardsLoadable';

const PublicCardsPage = () => {
  const setQuiz = useSetRecoilState(quizState);
  const setQuizMode = useSetRecoilState(quizModeState);
  const isLogin = useRecoilValue(userState) ? true : false;

  const { workbooks } = useWorkbook();
  const {
    workbookId,
    workbookName,
    cards,
    cardCount,
    tags,
    heartInfo,
    setHeartInfo,
    isAllCardChecked,
    checkAllCard,
    checkedCardCount,
    checkCard,
    takeCardsToMyWorkbook,
    isLoading,
    toggleHeart,
    showSnackbar,
  } = usePublicCard();

  const { routePublicSearchResultQuery } = useRouter();

  const { openModal, closeModal } = useModal();
  const { routeQuiz } = useRouter();

  const { heart, heartCount, serverHeart } = heartInfo;

  const onClickHeart = () => {
    if (!isLogin) {
      showSnackbar({ message: '로그인이 필요해요.' });

      return;
    }

    debounce(() => {
      if (serverHeart === !heart) return;

      toggleHeart();
    }, 200);

    setHeartInfo((prevValue) => ({
      ...prevValue,
      heart: !heart,
      heartCount: heart ? heartCount - 1 : heartCount + 1,
    }));
  };

  if (isLoading) {
    return <PublicCardsLoadable />;
  }

  return (
    <>
      <PageHeader
        title={ROUTE.PUBLIC_CARDS.TITLE}
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
            학습하기
          </StyledButton>
        }
      />
      <StyledPageTemplate isScroll={true}>
        <Heart>
          <button type="button" onClick={onClickHeart}>
            {heart ? (
              <FillHeartIcon
                width="1.5rem"
                height="1.5rem"
                fill={theme.color.red}
              />
            ) : (
              <EmptyHeartIcon width="1.5rem" height="1.5rem" />
            )}
          </button>
          <div>{heartCount}</div>
        </Heart>
        <TopContent>
          <WorkbookName>{workbookName}</WorkbookName>
          <CardCount>{cardCount}개의 카드</CardCount>
          <ul>
            {tags.map(({ id, name }) => (
              <TagWrapper key={id}>
                <Tag
                  type="button"
                  onClick={() =>
                    routePublicSearchResultQuery({
                      keyword: name,
                      method: 'push',
                    })
                  }
                >
                  <span>#</span>
                  {name}
                </Tag>
              </TagWrapper>
            ))}
          </ul>
        </TopContent>
        <ul>
          {cards.map(({ id, question, answer, isChecked }) => (
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

              if (!isLogin) {
                showSnackbar({ message: '로그인이 필요해요.' });

                return;
              }

              openModal({
                content: (
                  <PublicCardsSelectBox
                    publicWorkbookName={workbookName}
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
      </StyledPageTemplate>
    </>
  );
};

const StyledButton = styled(Button)`
  width: max-content;
  height: 2rem;

  ${({ theme }) =>
    css`
      font-size: ${theme.fontSize.small};
    `}
`;

const StyledPageTemplate = styled(PageTemplate)`
  position: relative;
  margin-bottom: 3rem;
  padding-top: 1.5rem;
`;

const Heart = styled.div`
  ${Flex({ direction: 'column', items: 'center' })};
  position: absolute;
  top: 2rem;
  right: 1.25rem;
`;

const TopContent = styled.div`
  width: calc(100% - 2rem);
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

const TagWrapper = styled.li`
  display: inline-block;
  word-break: break-all;
`;

const Tag = styled.button`
  margin-right: 0.5rem;
  text-align: left;

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
  width: 100%;
  left: 50%;
  transform: translateX(-50%);

  ${({ theme }) => css`
    max-width: ${theme.responsive.maxWidth};
  `}
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
