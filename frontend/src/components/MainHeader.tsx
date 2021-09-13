import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';
import { useRecoilValue } from 'recoil';

import LogoutIcon from '../assets/logout.svg';
import MenuIcon from '../assets/menu.svg';
import { CLOUD_FRONT_DOMAIN, DEVICE } from '../constants';
import { useRouter } from '../hooks';
import { userState } from '../recoil';
import { Flex } from '../styles';

const logoSrc = `${CLOUD_FRONT_DOMAIN}/logo.png`;
const userSrc = `${CLOUD_FRONT_DOMAIN}/user.png`;

interface MenuStyleProps {
  isMenuVisible: boolean;
}

const MainHeader = () => {
  const { routeMain, routeLogin, routeLogout, routeProfile } = useRouter();
  const userInfo = useRecoilValue(userState);
  const [isMenuVisible, setIsMenuVisible] = useState(false);

  return (
    <StyledHeader>
      <InnerContent>
        <h1>
          <Logo
            href="/"
            onClick={(event) => {
              event.preventDefault();
              setIsMenuVisible(false);
              routeMain();
            }}
          >
            <span>보고 또 보고</span>
          </Logo>
        </h1>
        <RightContent>
          {userInfo ? (
            <Navigation>
              <MenuIcon
                width="1.3rem"
                height="1.3rem"
                onClick={() => setIsMenuVisible((prevState) => !prevState)}
              />
              <button role="profile-link" onClick={routeProfile}>
                <Avatar src={userInfo?.profileUrl ?? userSrc} />
              </button>
              <button type="button" role="logout-button" onClick={routeLogout}>
                <div>로그아웃</div>
              </button>
            </Navigation>
          ) : (
            <button onClick={routeLogin}>로그인</button>
          )}
        </RightContent>
        <Menu isMenuVisible={isMenuVisible}>
          <button
            role="profile-link"
            onClick={() => {
              setIsMenuVisible(false);
              routeProfile();
            }}
          >
            <Avatar src={userInfo?.profileUrl ?? userSrc} />
            <div>{userInfo?.userName ?? 'Unknown User'}</div>
          </button>
          <button
            type="button"
            role="logout-button"
            onClick={() => {
              setIsMenuVisible(false);
              routeLogout();
            }}
          >
            <StyledLogoutIcon width={'1rem'} height={'1rem'} />
            <div>로그아웃</div>
          </button>
        </Menu>
      </InnerContent>
    </StyledHeader>
  );
};

const StyledHeader = styled.header`
  height: 3.75rem;
  padding: 0 0.75rem;
  z-index: 2;
  position: sticky;
  top: 0;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border-bottom: 1px solid ${theme.color.gray_3};
  `};
`;

const InnerContent = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  height: 100%;
  margin: 0 auto;

  ${({ theme }) => css`
    max-width: ${theme.responsive.maxWidth};
  `}
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

const Navigation = styled.div`
  & > button {
    display: none;
  }

  @media ${DEVICE.TABLET} {
    ${Flex({ items: 'center' })};

    & > svg {
      display: none;
    }

    & > button {
      ${Flex({ items: 'center' })};
      margin-left: 0.5rem;
    }
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
