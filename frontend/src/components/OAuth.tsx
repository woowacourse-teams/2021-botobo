import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import GithubIcon from '../assets/github-brands.svg';
import GoogleIcon from '../assets/google-logo.svg';
import { Flex } from '../styles';
import { AuthType } from '../types';

interface Props {
  type: AuthType;
  className?: string;
}

const codeRequestURI = {
  github: `https://github.com/login/oauth/authorize?client_id=${process.env.REACT_APP_GITHUB_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_GITHUB_LOGIN_REDIRECT_URL}`,
  google: `https://accounts.google.com/o/oauth2/auth?client_id=${process.env.REACT_APP_GOOGLE_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_GOOGLE_LOGIN_REDIRECT_URL}&response_type=code&scope=profile`,
};

const OAuth = ({ type, className }: Props) => (
  <OAuthLink href={codeRequestURI[type]} className={className}>
    {type === 'github' && (
      <>
        <GithubIcon width="1.5rem" height="1.5rem" />
        <span>Github으로 로그인</span>
      </>
    )}
    {type === 'google' && (
      <>
        <GoogleIcon width="1.5rem" height="1.5rem" />
        <span>Google로 로그인</span>
      </>
    )}
  </OAuthLink>
);

const OAuthLink = styled.a`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 100%;
  padding: 0.7rem;

  ${({ theme }) => css`
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.button};

    & > svg {
      margin-right: 1rem;
    }
  `}

  &:hover {
    filter: brightness(95%);
  }
`;

export default OAuth;
