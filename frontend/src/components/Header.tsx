import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import logo from '../assets/logo.svg';
import noUserImage from '../assets/no-user.svg';
import searchImage from '../assets/search.svg';
import { ROUTE } from '../constants';
import { Flex } from '../styles';

const Header = () => {
  const history = useHistory();

  return (
    <StyledHeader>
      <div onClick={() => history.push(ROUTE.HOME)}>
        <Logo src={logo} alt="로고" />
      </div>
      <RightContent>
        <button onClick={() => history.push(ROUTE.SEARCH)}>
          <SearchImage src={searchImage} alt="검색" />
        </button>
        <Avatar src={noUserImage} alt="유저 프로필" />
      </RightContent>
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
  cursor: pointer;
  height: 1.5rem;
`;

const RightContent = styled.div`
  ${Flex({ items: 'center' })}
`;

const SearchImage = styled.img`
  width: 1.3rem;
  height: 1.3rem;
`;

const Avatar = styled.img`
  width: 1.5rem;
  height: 1.5rem;
  margin-left: 1rem;

  ${({ theme }) => css`
    border-radius: ${theme.borderRadius.circle};
  `}
`;

export default Header;
