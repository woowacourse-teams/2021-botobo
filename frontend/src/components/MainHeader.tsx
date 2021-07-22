import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import { CLOUD_FRONT_DOMAIN, ROUTE, STORAGE_KEY } from '../constants';
import { loginState } from '../recoil';
import { Flex } from '../styles';
import { removeLocalStorage, setSessionStorage } from '../utils';

const logoSrc = `${CLOUD_FRONT_DOMAIN}/logo.png`;
const userSrc = `${CLOUD_FRONT_DOMAIN}/user.png`;

const MainHeader = () => {
  const history = useHistory();
  const [isLogin, setIsLogin] = useRecoilState(loginState);

  const routeLogin = () => {
    setSessionStorage(STORAGE_KEY.REDIRECTED_PATH, window.location.pathname);
    history.push(ROUTE.LOGIN.PATH);
  };

  const logout = () => {
    removeLocalStorage(STORAGE_KEY.TOKEN);
    setIsLogin(false);
    history.push(ROUTE.HOME.PATH);
  };

  return (
    <StyledHeader>
      <h1>
        <Logo
          href="/"
          onClick={(event) => {
            event.preventDefault();
            history.push(ROUTE.HOME.PATH);
          }}
        >
          <span>보고 또 보고</span>
        </Logo>
      </h1>
      <RightContent>
        {isLogin ? (
          <>
            <Avatar src={userSrc} />
            <AuthButton onClick={logout}>로그아웃</AuthButton>
          </>
        ) : (
          <AuthButton onClick={routeLogin}>로그인</AuthButton>
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

const AuthButton = styled.button`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

export default MainHeader;
