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
      '보고 또 보고는 자신이 추가한 문제집을 통해 퀴즈를 풀며 복습할 수 있는 서비스입니다.\n퀴즈를 풀기 전에 로그인 후 자신만의 문제집을 등록해보세요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide1.png`,
  },
  {
    description: '문제집 제목과 태그를 추가하여 문제집을 추가할 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide2.png`,
  },
  {
    description: '추가된 문제집에 카드를 추가해볼까요?',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide3.png`,
  },
  {
    description: '카드 등록이 완료되었어요. 등록된 카드로 퀴즈를 풀어보세요!',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide4.png`,
  },
  {
    description: '퀴즈를 풀어볼까요?',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide5.png`,
  },
  {
    description:
      '다른 사람들의 문제집을 검색해 퀴즈를 풀어보거나 나만의 문제집으로 만들 수 있어요!',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide6.png`,
  },
];

const Guide = () => (
  <>
    <MainHeader />
    <PageTemplate isScroll={true}>
      {guides.map(({ description, imgSrc }, index) => (
        <div key={index}>
          <span>{formatNewLine(description)}</span>
          <Image src={imgSrc} alt={description} />
        </div>
      ))}
    </PageTemplate>
  </>
);

const Image = styled.img`
  margin-top: 0.5rem;
  margin-bottom: 1.3rem;

  ${({ theme }) => css`
    width: 100%;
    box-shadow: ${theme.boxShadow.card};
  `}
`;

export default Guide;
