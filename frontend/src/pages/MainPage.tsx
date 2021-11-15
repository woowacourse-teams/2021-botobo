import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import BronzeMedalIcon from '../assets/bronze-medal.svg';
import ForwardIcon from '../assets/chevron-right-solid.svg';
import GoldMedalIcon from '../assets/gold-medal.svg';
import SilverMedalIcon from '../assets/silver-medal.svg';
import {
  Button,
  Confirm,
  MainHeader,
  QuizStarter,
  Workbook,
} from '../components';
import CardTemplate from '../components/CardTemplate';
import { DEVICE, ROUTE } from '../constants';
import { useModal, useRouter, useWorkbook } from '../hooks';
import {
  publicSearchResultState,
  shouldWorkbookUpdateState,
  userState,
} from '../recoil';
import { Flex, WorkbookListStyle } from '../styles';
import PageTemplate from './PageTemplate';

const rankingDecorator = {
  1: { icon: <GoldMedalIcon />, color: 'gold' },
  2: { icon: <SilverMedalIcon />, color: 'silver' },
  3: { icon: <BronzeMedalIcon />, color: 'bronze' },
} as const;

const MainPage = () => {
  const userInfo = useRecoilValue(userState);
  const {
    workbooks,
    workbookRankings,
    searchKeywordRankings,
    deleteWorkbook,
    updateWorkbooks,
    getRankings,
  } = useWorkbook();
  const {
    routeWorkbookAdd,
    routeWorkbookEdit,
    routePublicSearch,
    routeGuide,
    routePublicSearchResultQuery,
  } = useRouter();
  const shouldWorkbookUpdate = useRecoilValue(shouldWorkbookUpdateState);
  const resetSearchResult = useResetRecoilState(publicSearchResultState);

  const { openModal } = useModal();

  useEffect(() => {
    getRankings();

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
        <TemporaryWrapper>
          <Container>
            <Inner>
              <TitleContent>
                <h2>🚨 긴급공지 🚨</h2>
              </TitleContent>
              <Content>
                <span>
                  안녕하세요, 보고 또 보고 운영진입니다. 아쉽게도 보고 또 보고는{' '}
                  <strong>11월 26일부로 서비스가 종료됩니다.</strong> 다시 운영
                  할 때를 대비하여 저희도 데이터를 백업할 예정이지만, 서비스를
                  사용해주시는 분들이 개인적으로 문제집을 소장하실 수 있도록
                  다운로드 기능을 준비했어요. 로그인 하신 다음,{' '}
                  {/* eslint-disable-next-line react/no-unescaped-entities */}
                  <strong>"내 문제집 다운로드 하기"</strong>
                  버튼을 눌러서 소중한 문제집을 다음에 또 볼 수 있도록
                  보관해보세요.
                  <br />
                  <br />
                  지금까지 보고 또 보고를 사랑해주셔서 정말 감사합니다.
                  <br />
                  다음에 더욱 발전한 모습으로 찾아뵙도록 할게요. 모두 즐거운
                  연말 보내세요 :)
                </span>
              </Content>
            </Inner>
          </Container>
          {userInfo && (
            <TemporaryBanner onClick={downloadWorkbooks}>
              <BannerText>내 문제집 다운로드 하기</BannerText>
              <StyledButton
                backgroundColor="white"
                color="gray_8"
                shape="circle"
              >
                <ForwardIcon width="1rem" height="1rem" />
              </StyledButton>
            </TemporaryBanner>
          )}
        </TemporaryWrapper>
        <Banner onClick={routePublicSearch}>
          <BannerText>다양한 문제집 보러 가기</BannerText>
          <StyledButton backgroundColor="white" color="gray_8" shape="circle">
            <ForwardIcon width="1rem" height="1rem" />
          </StyledButton>
        </Banner>
        <QuizStarter workbooks={workbooks} />
        <RankingWrapper>
          {searchKeywordRankings.length > 0 && (
            <div>
              <Title>인기 검색어</Title>
              <SearchKeywordRankingList>
                {searchKeywordRankings.map(({ rank, keyword }) => (
                  <li key={rank}>
                    <SearchKeywordRankingButton
                      shape="round"
                      color="black"
                      backgroundColor={rankingDecorator[rank].color}
                      onClick={async () => {
                        await resetSearchResult();
                        routePublicSearchResultQuery({
                          keyword,
                          method: 'push',
                        });
                      }}
                    >
                      <span># {keyword}</span>
                    </SearchKeywordRankingButton>
                  </li>
                ))}
              </SearchKeywordRankingList>
            </div>
          )}
          {workbookRankings.length > 0 && (
            <div>
              <Title>인기 문제집</Title>
              <WorkbookRankingList>
                {workbookRankings.map(({ id, ...rest }, index) => {
                  const rank = (index + 1) as 1 | 2 | 3;

                  return (
                    <WorkbookRankingItem key={id}>
                      <RankingIconWrapper>
                        {rankingDecorator[rank].icon}
                      </RankingIconWrapper>
                      <Workbook
                        path={`${ROUTE.PUBLIC_CARDS.PATH}/${id}`}
                        {...rest}
                      />
                    </WorkbookRankingItem>
                  );
                })}
                <WorkbookRankingText>
                  인기 문제집의 주인공이 되어보세요!
                </WorkbookRankingText>
              </WorkbookRankingList>
            </div>
          )}
        </RankingWrapper>
        <section>
          <WorkbookHeader>
            <Title>학습 중</Title>
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

const Container = styled(CardTemplate)`
  height: 15rem;
  margin-bottom: 1rem;

  ${({ theme }) => css`
    background-color: ${theme.color.pink_2}20;
    border-radius: ${theme.borderRadius.square};
    color: ${theme.color.gray_9};
  `};
`;

const Inner = styled.div`
  ${Flex({ direction: 'column', justify: 'space-evenly' })};
  max-width: 37.5rem;
  height: 100%;
  margin: 0 auto;
`;

const TitleContent = styled.div`
  ${Flex({ direction: 'column', justify: 'center' })};
  length: 100%;
  text-align: center;

  & > span {
    margin-bottom: 1rem;
  }

  ${({ theme }) => css`
    color: ${theme.color.red};
  `};
`;

const Content = styled.div`
  ${Flex({ direction: 'column', justify: 'center' })};
  length: 100%;

  & > span {
    margin-bottom: 1rem;
  }
`;

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

const TemporaryWrapper = styled.div`
  ${Flex({ direction: 'column', justify: 'space-between' })};
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

const TemporaryBanner = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  cursor: pointer;
  height: 3rem;
  margin-bottom: 1rem;
  padding: 0 1rem;

  ${({ theme }) => css`
    color: ${theme.color.white};
    background-color: ${theme.color.pink_2};
    border-radius: ${theme.borderRadius.square};
    font-weight: ${theme.fontWeight.semiBold};
  `}
`;

const BannerText = styled.span`
  word-spacing: -2px;
`;

const Title = styled.h2`
  ${({ theme }) =>
    css`
      font-size: ${theme.fontSize.semiLarge};
    `};
`;

const RankingWrapper = styled.section`
  margin-top: 2rem;
`;

const SearchKeywordRankingList = styled.ul`
  ${Flex()};
  overflow-x: auto;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
  padding-bottom: 1rem;

  & > li:not(:last-of-type) {
    margin-right: 1rem;
  }
`;

const SearchKeywordRankingButton = styled(Button)`
  padding-left: 1rem;
  padding-right: 1rem;
  width: max-content;

  ${({ theme }) => css`
    font-weight: ${theme.fontWeight.semiBold};
  `}
`;

const WorkbookRankingList = styled.ul`
  ${WorkbookListStyle};
  margin-top: 1.25rem;
`;

const WorkbookRankingItem = styled.li`
  position: relative;
`;

const RankingIconWrapper = styled.div`
  position: absolute;
  z-index: 1;
  top: -0.75rem;
  left: -0.75rem;
`;

const WorkbookRankingText = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  margin-top: 0.5rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}

  @media ${DEVICE.TABLET} {
    margin-top: 0;
  }
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
