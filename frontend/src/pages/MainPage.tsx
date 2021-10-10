import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import ForwardIcon from '../assets/chevron-right-solid.svg';
import {
  Button,
  Confirm,
  MainHeader,
  QuizStarter,
  Workbook,
} from '../components';
import { DEVICE, ROUTE } from '../constants';
import { useModal, useRouter, useWorkbook } from '../hooks';
import { shouldWorkbookUpdateState, userState } from '../recoil';
import { Flex, WorkbookListStyle } from '../styles';
import PageTemplate from './PageTemplate';

const MainPage = () => {
  const userInfo = useRecoilValue(userState);
  const { workbooks, deleteWorkbook, updateWorkbooks } = useWorkbook();
  const { routeWorkbookAdd, routeWorkbookEdit, routePublicSearch, routeGuide } =
    useRouter();
  const shouldWorkbookUpdate = useRecoilValue(shouldWorkbookUpdateState);

  const { openModal } = useModal();

  useEffect(() => {
    if (!shouldWorkbookUpdate) return;

    updateWorkbooks();
  }, []);

  return (
    <>
      <MainHeader />
      <PageTemplate isScroll={true}>
        <GreetingWrapper>
          {userInfo && <span>안녕하세요, {userInfo.userName} 님!</span>}
          <Guide onClick={routeGuide}>보고 또 보고 이용 방법</Guide>
        </GreetingWrapper>
        <Banner onClick={routePublicSearch}>
          <BannerText>다양한 문제집 보러 가기</BannerText>
          <StyledButton backgroundColor="white" color="gray_8" shape="circle">
            <ForwardIcon width="1rem" height="1rem" />
          </StyledButton>
        </Banner>
        <QuizStarter workbooks={workbooks} />
        <section>
          <WorkbookHeader>
            <WorkbookTitle>학습 중</WorkbookTitle>
            <Button shape="square" onClick={routeWorkbookAdd}>
              문제집 추가
            </Button>
          </WorkbookHeader>
          {workbooks.length === 0 ? (
            <NoWorkbook>
              {userInfo
                ? '아직 추가된 문제집이 없어요.'
                : '로그인 후 나만의 문제집을 만들어보세요.'}
            </NoWorkbook>
          ) : (
            <StyledUl>
              {workbooks.map(({ id, name, cardCount, heartCount, tags }) => (
                <li key={id}>
                  <Workbook
                    heartCount={heartCount}
                    tags={tags}
                    name={name}
                    path={`${ROUTE.CARDS.PATH}/${id}`}
                    cardCount={cardCount}
                    editable={true}
                    onClickEditButton={() => routeWorkbookEdit(id)}
                    onClickDeleteButton={() =>
                      openModal({
                        content: (
                          <Confirm onConfirm={() => deleteWorkbook(id)}>
                            해당 문제집을 정말 삭제하시겠어요?
                          </Confirm>
                        ),
                        type: 'center',
                      })
                    }
                  />
                </li>
              ))}
            </StyledUl>
          )}
        </section>
      </PageTemplate>
    </>
  );
};

const GreetingWrapper = styled.div`
  ${Flex({ direction: 'column', justify: 'space-between' })};
  margin-bottom: 1rem;

  & > span {
    margin-bottom: 0.5rem;
  }

  @media ${DEVICE.TABLET} {
    ${Flex({ justify: 'space-between', items: 'center' })};

    & > span {
      margin-bottom: 0;
    }
  }
`;

const Guide = styled.button`
  padding: 0.7rem 1.3rem;
  width: 100%;

  ${({ theme }) => css`
    background-color: ${theme.color.green}20;
    border-radius: ${theme.borderRadius.square};
    color: ${theme.color.green};

    &:hover {
      background-color: ${theme.color.green}30;
    }
  `};

  @media ${DEVICE.TABLET} {
    width: max-content;
  }
`;

const Banner = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  cursor: pointer;
  height: 3rem;
  margin-bottom: 1rem;
  padding: 0 1rem;

  ${({ theme }) => css`
    color: ${theme.color.white};
    background-color: ${theme.color.blue};
    border-radius: ${theme.borderRadius.square};
    font-weight: ${theme.fontWeight.semiBold};
  `}
`;

const BannerText = styled.span`
  word-spacing: -2px;
`;

const StyledButton = styled(Button)`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 1.5rem;
  height: 1.5rem;
  padding: 0;

  & > svg {
    margin-left: 0.1rem;
  }
`;

const WorkbookHeader = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-top: 3rem;
`;

const WorkbookTitle = styled.h2`
  ${({ theme }) =>
    css`
      font-size: ${theme.fontSize.semiLarge};
    `};
`;

const NoWorkbook = styled.div`
  text-align: center;
  margin-top: 10vh;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;

const StyledUl = styled.ul`
  ${WorkbookListStyle};
  margin: 1rem 0;
`;

export default MainPage;
