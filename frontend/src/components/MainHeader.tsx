import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';
import { useRecoilValue } from 'recoil';

import LogoutIcon from '../assets/logout.svg';
import MenuIcon from '../assets/menu.svg';
import { CLOUD_FRONT_DOMAIN } from '../constants';
import { useRouter } from '../hooks';
import { userState } from '../recoil';
import { Flex } from '../styles';

const logoSrc = `${CLOUD_FRONT_DOMAIN}/logo.png`;
const userSrc = `${CLOUD_FRONT_DOMAIN}/user.png`;

interface Props {
  sticky?: boolean;
  shadow?: boolean;
}

interface MenuStyleProps {
  isMenuVisible: boolean;
}

const MainHeader = ({ sticky = true, shadow = true }: Props) => {
  const { routeMain, routeLogin, routeLogout, routeProfile } = useRouter();
  const userInfo = useRecoilValue(userState);
  const [isMenuVisible, setIsMenuVisible] = useState(false);

  return (
    <StyledHeader sticky={sticky} shadow={shadow}>
      <h1>
        <Logo
          href="/"
          onClick={(event) => {
            event.preventDefault();
            routeMain();
          }}
        >
          <span>보고 또 보고</span>
        </Logo>
      </h1>
      <RightContent>
        {userInfo ? (
          <MenuIcon
            width="1.3rem"
            height="1.3rem"
            onClick={() => setIsMenuVisible((prevState) => !prevState)}
          />
        ) : (
          <button onClick={routeLogin}>로그인</button>
        )}
      </RightContent>
      <Menu isMenuVisible={isMenuVisible}>
        <button role="profile-link" onClick={routeProfile}>
          <Avatar src={userInfo?.profileUrl ?? userSrc} />
          <div>{userInfo?.userName ?? 'Unknown User'}</div>
        </button>
        <button type="button" role="logout-button" onClick={routeLogout}>
          <StyledLogoutIcon width={'1rem'} height={'1rem'} />
          <div>로그아웃</div>
        </button>
      </Menu>
    </StyledHeader>
  );
};

const StyledHeader = styled.header<Required<Props>>`
  ${Flex({ justify: 'space-between', items: 'center' })};
  height: 3.75rem;
  padding: 0 0.75rem;
  z-index: 1;
  position: relative;

  ${({ theme, sticky, shadow }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${shadow ? theme.boxShadow.header : ''};
    z-index: 2;

    ${sticky
      ? css`
          position: sticky;
          top: 0;
        `
      : ''}
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

  & > svg {
    margin-left: 0.7rem;
    cursor: pointer;
  }
`;

const Menu = styled.nav<MenuStyleProps>`
  position: absolute;
  top: 3.75rem;
  left: 0;
  width: 100%;
  height: 0;
  padding: 0 0.75rem;
  overflow: hidden;

  ${({ theme, isMenuVisible }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};
    transition: height 0.2s ease;

    ${isMenuVisible &&
    css`
      height: 6.25rem;
    `}

    & > * {
      ${Flex({ items: 'center' })};
      width: 100%;
      height: 3rem;
      border-top: 1px solid ${theme.color.gray_4};
      padding: 0.7rem 0;
    }
  `}
`;

const Avatar = styled.img`
  width: 1.5rem;
  height: 1.5rem;
  margin-right: 0.3rem;

  ${({ theme }) => css`
    border-radius: ${theme.borderRadius.circle};
  `}
`;

const StyledLogoutIcon = styled(LogoutIcon)`
  margin-right: 0.3rem;
`;

export default MainHeader;
