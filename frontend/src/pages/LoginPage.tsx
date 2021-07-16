import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import logoSrc from '../assets/logo-full.png';
import { OAuth } from '../components';
import { Flex } from '../styles';

const LoginPage = () => (
  <Container>
    <Logo src={logoSrc} alt="로고 이미지" />
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

const Logo = styled.img`
  margin-top: 5rem;
`;

const AuthWrapper = styled.div`
  width: 100%;
  padding: 2rem;
`;

export default LoginPage;
