import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import ForwardIcon from '../assets/chevron-right-solid.svg';
import { Button, MainHeader, QuizStarter, WorkbookList } from '../components';
import { STORAGE_KEY } from '../constants';
import { useRouter, useWorkbook } from '../hooks';
import { shouldWorkbookUpdateState, userState } from '../recoil';
import { Flex } from '../styles';
import { setSessionStorage } from '../utils';

const MainPage = () => {
  const userInfo = useRecoilValue(userState);
  const { workbooks, setWorkbookId, deleteWorkbook, updateWorkbooks } =
    useWorkbook();
  const { routeWorkbookAdd, routeCards, routePublicSearch } = useRouter();
  const shouldWorkbookUpdate = useRecoilValue(shouldWorkbookUpdateState);

  useEffect(() => {
    if (!shouldWorkbookUpdate) return;

    updateWorkbooks();
  }, []);

  return (
    <>
      <MainHeader />
      <Container>
        {userInfo && <Greeting>안녕하세요, {userInfo.userName} 님!</Greeting>}
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
            <NoWorkbook>아직 추가된 문제집이 없어요.</NoWorkbook>
          ) : (
            <WorkbookList
              workbooks={workbooks}
              onClickWorkbook={async (id) => {
                await setWorkbookId(id);
                setSessionStorage(STORAGE_KEY.WORKBOOK_ID, id);
                routeCards();
              }}
              editable={true}
              deleteWorkbook={deleteWorkbook}
            />
          )}
        </section>
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

const Greeting = styled.div`
  margin-bottom: 1rem;
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
  ${Flex({ justify: 'center', items: 'center' })}
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
  margin-top: 20vh;
`;

export default MainPage;
