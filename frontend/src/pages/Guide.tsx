import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { MainHeader } from '../components';
import { CLOUD_FRONT_DOMAIN } from '../constants';
import { formatNewLine } from '../utils';
import PageTemplate from './PageTemplate';

interface Guide {
  description: string;
  imgSrc: string;
}

const guides: Guide[] = [
  {
    description:
      '보고 또 보고는 간편한 개념 정리와 퀴즈 방식의 학습을 도와주는 서비스입니다. 퀴즈를 풀기 전에 로그인 후 자신만의 문제집을 등록해보세요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide1.png`,
  },
  {
    description: '문제집 제목과 태그를 입력해 문제집을 등록할 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide2.png`,
  },
  {
    description: '문제집에 카드를 추가해볼까요?',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide3.png`,
  },
  {
    description: '카드 추가가 완료되었어요. 추가된 카드로 퀴즈를 풀어보세요!',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide4.png`,
  },
  {
    description: '퀴즈 설정을 통해 원하는 만큼 학습이 가능해요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide5.png`,
  },
  {
    description:
      '더 많은 지식을 찾아보고 싶다면, 다양한 문제집 보러가기도 이용해 보세요!',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide6.png`,
  },
];

const Guide = () => (
  <>
    <MainHeader />
    <PageTemplate isScroll={true}>
      <Title>보고 또 보고 이용 방법</Title>
      {guides.map(({ description, imgSrc }, index) => (
        <div key={index}>
          <span>
            {index + 1}. {formatNewLine(description)}
          </span>
          <Image src={imgSrc} alt={description} />
        </div>
      ))}
    </PageTemplate>
  </>
);

const Title = styled.h2`
  margin-bottom: 2rem;
`;

const Image = styled.img`
  margin-top: 1rem;
  margin-bottom: 3rem;

  ${({ theme }) => css`
    width: 100%;
    box-shadow: ${theme.boxShadow.card};
  `}
`;

export default Guide;
