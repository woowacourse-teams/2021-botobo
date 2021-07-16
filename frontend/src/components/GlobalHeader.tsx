import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import logoSrc from '../assets/logo.png';
import SearchIcon from '../assets/search.svg';
import userSrc from '../assets/user.png';
import { ROUTE } from '../constants';
import { loginState } from '../recoil';
import { Flex } from '../styles';

const GlobalHeader = () => {
  const history = useHistory();
  const isLogin = useRecoilValue(loginState);

  return (
    <StyledHeader>
      <h1>
        <Logo
          href="/"
          onClick={(event) => {
            event.preventDefault();
            history.push(ROUTE.HOME);
          }}
        >
          <span>보고 또 보고</span>
        </Logo>
      </h1>
      <RightContent>
        <button onClick={() => history.push(ROUTE.SEARCH)}>
          <SearchIcon width="1.25rem" height="1.25rem" />
        </button>
        {isLogin && <Avatar src={userSrc} />}
        {isLogin ? (
          <button>로그아웃</button>
        ) : (
          <button onClick={() => history.push(ROUTE.LOGIN)}>로그인</button>
        )}
      </RightContent>
    </StyledHeader>
  );
};

const StyledHeader = styled.header`
  ${Flex({ justify: 'space-between', items: 'center' })};
  height: 3.75rem;
  padding: 0 0.75rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};
  `};
`;

const Logo = styled.a`
  position: relative;
  height: 1.5rem;

  &::before {
    content: '';
    display: inline-block;
    vertical-align: middle;
    height: 2rem;
    width: 8rem;
    background-repeat: no-repeat;
    background-size: contain;
    background-image: url(${logoSrc});
  }

  & > span {
    position: absolute;
    clip: rect(0 0 0 0);
    width: 1px;
    height: 1px;
    margin: -1px;
    overflow: hidden;
  }
`;

const RightContent = styled.div`
  ${Flex({ items: 'center' })};

  & > * {
    margin-left: 0.7rem;
  }
`;

const Avatar = styled.img`
  width: 1.5rem;
  height: 1.5rem;

  ${({ theme }) => css`
    border-radius: ${theme.borderRadius.circle};
  `}
`;

export default GlobalHeader;
