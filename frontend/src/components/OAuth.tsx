import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import GithubIcon from '../assets/github-brands.svg';
import { Flex } from '../styles';

const CLIENT_ID = process.env.REACT_APP_GITHUB_CLIENT_ID;
const REDIRECT_URI = process.env.REACT_APP_LOGIN_REDIRECT_URL;
const URI = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}`;

const OAuth = () => {
  return (
    <OAuthLink href={URI}>
      <GithubIcon width="1.5rem" height="1.5rem" />
      <span>Github으로 로그인</span>
    </OAuthLink>
  );
};

const OAuthLink = styled.a`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 100%;
  padding: 0.7rem;

  ${({ theme }) => css`
    background-color: ${theme.color.gray_8};
    border-radius: ${theme.borderRadius.square};

    & > * {
      color: ${theme.color.white};
    }

    & > svg {
      margin-right: 1rem;
    }
  `}

  &:hover {
    filter: brightness(95%);
  }
`;

export default OAuth;
