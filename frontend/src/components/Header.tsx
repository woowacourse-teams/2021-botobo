import styled from '@emotion/styled';
import React from 'react';

import logo from '../assets/logo.png';
import noUserImage from '../assets/no_user_image.png';
import { Flex } from '../styles';

const Header = () => (
  <StyledHeader>
    <div>
      <Logo src={logo} alt="로고" />
    </div>
    <Profile>
      <Avatar src={noUserImage} alt="유저 프로필" />
    </Profile>
  </StyledHeader>
);

const StyledHeader = styled.header`
  ${Flex({ justify: 'space-between', items: 'center' })}
  background-color: ${({ theme }) => theme.color.white};
  height: 3.75rem;
  padding: 0 0.75rem;
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
