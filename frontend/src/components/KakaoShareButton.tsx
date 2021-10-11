import styled from '@emotion/styled';
import React, { useEffect } from 'react';

import ShareIcon from '../assets/share.svg';

interface Props {
  title: string;
  path: string;
}

const KakaoShareButton = ({ title, path }: Props) => {
  const share = () => {
    if (!window.Kakao.isInitialized()) return;

    window.Kakao.Link.sendCustom({
      templateId: 63302,
      templateArgs: {
        title,
        description: '문제집이 공유되었어요. 확인해보세요!',
        path,
      },
      installTalk: true,
    });
  };

  useEffect(() => {
    if (window.Kakao.isInitialized()) return;
    if (!process.env.REACT_APP_KAKAO_API_KEY) return;

    window.Kakao.init(process.env.REACT_APP_KAKAO_API_KEY);
  }, []);

  return (
    <Button onClick={share}>
      <ShareIcon />
      <div>공유하기</div>
    </Button>
  );
};

const Button = styled.button`
  width: max-content;
`;

export default KakaoShareButton;
