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
      <h2>{title}</h2>
    </StyledHeader>
  );
};

const StyledHeader = styled.header`
  ${Flex({ items: 'center' })};
  height: 3.75rem;
  padding: 0 0.75rem;

  ${({ theme }) => css`
    box-shadow: ${theme.boxShadow.header};
  `};
`;

const PageBackButton = styled.button`
  margin-right: 1rem;
`;

export default PageHeader;
