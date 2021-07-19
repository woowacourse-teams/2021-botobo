import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { OAuth } from '../components';
import { CLOUD_FRONT_DOMAIN } from '../constants';
import { Flex } from '../styles';

const logoSrc = `${CLOUD_FRONT_DOMAIN}/logo-full.png`;

const LoginPage = () => (
  <Container>
    <LogoWrapper>
      <img src={logoSrc} alt="로고 이미지" />
    </LogoWrapper>
    <AuthWrapper>
      <OAuth />
    </AuthWrapper>
  </Container>
);

const Container = styled.div`
  ${Flex({ direction: 'column', items: 'center' })};

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      height: ${theme.pageSize.height};
    `}
`;

const LogoWrapper = styled.div`
  margin-top: 5rem;
  height: 6.25rem;
`;

const AuthWrapper = styled.div`
  width: 100%;
  padding: 2rem;
`;

export default LoginPage;
