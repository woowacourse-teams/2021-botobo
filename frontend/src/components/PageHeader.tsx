import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import BackIcon from '../assets/chevron-left-solid.svg';
import { Flex } from '../styles';

interface Props {
  title: string;
}

const PageHeader = ({ title }: Props) => {
  const history = useHistory();

  return (
    <StyledHeader>
      <PageBackButton onClick={history.goBack}>
        <BackIcon width="1.5rem" height="1.5rem" />
      </PageBackButton>
      <Title>{title}</Title>
    </StyledHeader>
  );
};

const StyledHeader = styled.header`
  ${Flex({ items: 'center' })};
  height: 3.75rem;
  padding: 0 0.75rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};
  `};
`;

const PageBackButton = styled.button`
  margin-right: 1rem;
`;

const Title = styled.h1`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;

export default PageHeader;
