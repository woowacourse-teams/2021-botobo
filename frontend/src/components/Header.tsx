import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import logo from '../assets/logo.png';
import noUserImage from '../assets/no_user_image.png';
import { ROUTE } from '../constants';
import { Flex } from '../styles';

const Header = () => {
  const history = useHistory();

  return (
    <StyledHeader>
      <div onClick={() => history.push(ROUTE.HOME)}>
        <Logo src={logo} alt="로고" />
      </div>
      <Profile>
        <Avatar src={noUserImage} alt="유저 프로필" />
      </Profile>
    </StyledHeader>
  );
};

const StyledHeader = styled.header`
  ${Flex({ justify: 'space-between', items: 'center' })}
  height: 3.75rem;
  padding: 0 0.75rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};
  `};
`;

const Logo = styled.img`
  height: 2rem;
`;

const Profile = styled.div``;

const Avatar = styled.img`
  height: 2rem;
  border-radius: ${({ theme }) => theme.borderRadius.circle};
`;

export default Header;
