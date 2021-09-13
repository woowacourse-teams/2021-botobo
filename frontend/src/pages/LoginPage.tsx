import styled from '@emotion/styled';
import React from 'react';

import { OAuth } from '../components';
import { CLOUD_FRONT_DOMAIN } from '../constants';
import { Flex } from '../styles';
import PageTemplate from './PageTemplate';

const logoSrc = `${CLOUD_FRONT_DOMAIN}/logo-full.png`;

const LoginPage = () => (
  <StyledPageTemplate isScroll={false}>
    <LogoWrapper>
      <img src={logoSrc} alt="로고 이미지" />
    </LogoWrapper>
    <AuthWrapper>
      <OAuth type="github" />
      <OAuth type="google" />
    </AuthWrapper>
  </StyledPageTemplate>
);

const StyledPageTemplate = styled(PageTemplate)`
  ${Flex({ direction: 'column', items: 'center' })};
`;

const LogoWrapper = styled.div`
  margin-top: 5rem;
  height: 6.25rem;
`;

const AuthWrapper = styled.div`
  width: 100%;
  padding: 2rem;

  & > a {
    margin-top: 1rem;
  }
`;

export default LoginPage;
