import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import GithubIcon from '../assets/github-brands.svg';
import GoogleIcon from '../assets/google-logo.svg';
import { STORAGE_KEY, theme } from '../constants';
import { Flex } from '../styles';
import { AuthType } from '../types';
import { setSessionStorage } from '../utils';

interface Props {
  type: AuthType;
}

interface OAuthLinkStyleProps {
  type: AuthType;
}

const codeRequestURI = {
  github: `https://github.com/login/oauth/authorize?client_id=${process.env.REACT_APP_GITHUB_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_GITHUB_LOGIN_REDIRECT_URL}`,
  google: `https://accounts.google.com/o/oauth2/auth?client_id=${process.env.REACT_APP_GOOGLE_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_GOOGLE_LOGIN_REDIRECT_URL}&response_type=code&scope=profile`,
};

const oauthStyle = {
  github: css`
    background-color: ${theme.color.gray_8};

    & > * {
      color: ${theme.color.white};
    }
  `,
  google: css`
    background-color: ${theme.color.white};
  `,
};

const OAuth = ({ type }: Props) => (
  <OAuthLink
    href={codeRequestURI[type]}
    type={type}
    onClick={() => setSessionStorage(STORAGE_KEY.SOCIAL_TYPE, type)}
  >
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

const OAuthLink = styled.a<OAuthLinkStyleProps>`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 100%;
  padding: 0.7rem;

  ${({ theme, type }) => css`
    ${oauthStyle[type]};

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
