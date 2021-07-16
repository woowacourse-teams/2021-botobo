import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import GithubIcon from '../assets/github-brands.svg';
import { ROUTE } from '../constants';
import { Flex } from '../styles';

const CLIENT_ID = 'c7b8dfc709b50e0c7885';
const REDIRECT_URI = `http://localhost:3000${ROUTE.GITHUB_CALLBACK}`;
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
