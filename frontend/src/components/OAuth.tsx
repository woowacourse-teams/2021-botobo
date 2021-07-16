import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { getAccessTokenAsync } from '../api';
import GithubIcon from '../assets/github-brands.svg';
import { ROUTE } from '../constants';
import { loginState } from '../recoil';
import { Flex } from '../styles';

const GITHUB_OAUTH_QUERY = 'code';

const url =
  'https://github.com/login/oauth/authorize?client_id=c7b8dfc709b50e0c7885&redirect_uri=http://localhost:3000/';

const OAuth = () => {
  const history = useHistory();
  const { search } = useLocation();
  const setIsLogin = useSetRecoilState(loginState);
  const code = new URLSearchParams(search).get(GITHUB_OAUTH_QUERY);

  useEffect(() => {
    if (!code) return;

    (async () => {
      try {
        const accessToken = await getAccessTokenAsync(code);

        setIsLogin(true);
      } catch (error) {
        console.error(error);
      }
    })();
  }, [code, history]);

  return (
    <OAuthLink href={url}>
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
