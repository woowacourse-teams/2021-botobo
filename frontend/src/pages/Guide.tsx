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
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image1.png`,
  },
  {
    description: '문제집 제목과 태그를 입력해 문제집을 등록할 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image2.png`,
  },
  {
    description: '문제집에 카드를 추가해볼까요?',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image3.png`,
  },
  {
    description: '원하는 질문과 답변을 작성할 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image4.png`,
  },
  {
    description:
      '카드가 추가된 문제집을 이용해, 퀴즈를 생성할 수 있어요. 원하는 대로 퀴즈를 설정해보아요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image5.png`,
  },
  {
    description: '퀴즈를 풀어보아요! 퀴즈를 누르면 답을 확인할 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image6.png`,
  },
  {
    description:
      '퀴즈 결과 화면에서, 답변이 아쉬웠던 문제를 다음에 또 풀도록 설정할 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image7.png`,
  },
  {
    description:
      '더 많은 지식을 찾아보고 싶다면, 다양한 문제집 보러가기도 이용해 보세요! 검색을 통해 원하는 문제집을 찾아보아요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image8.png`,
  },
  {
    description:
      '검색 결과에서도 필터링을 통해 원하는 문제집을 더 자세히 찾을 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image9.png`,
  },
  {
    description:
      '마음에 드는 문제집을 확인했다면, 내 문제집으로 가져가기를 통해 나의 문제집으로 만들 수 있어요.',
    imgSrc: `${CLOUD_FRONT_DOMAIN}/guide_image10.png`,
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
          <ImageWrapper>
            <Image src={imgSrc} alt={description} />
          </ImageWrapper>
        </div>
      ))}
    </PageTemplate>
  </>
);

const Title = styled.h2`
  margin-bottom: 2rem;
`;

const ImageWrapper = styled.div`
  height: 34.375rem;
`;

const Image = styled.img`
  margin-top: 1rem;
  margin-bottom: 3rem;
  width: 100%;
`;

export default Guide;
