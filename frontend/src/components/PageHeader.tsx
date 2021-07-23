import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import BackIcon from '../assets/chevron-left-solid.svg';
import { useRouter } from '../hooks';
import { Flex } from '../styles';

interface Props {
  title: string;
  rightContent?: React.ReactElement;
  sticky?: boolean;
}

interface StyledHeaderProps {
  sticky: boolean;
}

const PageHeader = ({ title, rightContent, sticky = false }: Props) => {
  const { routePrevPage } = useRouter();

  return (
    <StyledHeader sticky={sticky}>
      <LeftContent>
        <PageBackButton onClick={routePrevPage}>
          <BackIcon width="1.5rem" height="1.5rem" />
        </PageBackButton>
        <Title>{title}</Title>
      </LeftContent>
      {rightContent}
    </StyledHeader>
  );
};

const StyledHeader = styled.header<StyledHeaderProps>`
  ${Flex({ items: 'center', justify: 'space-between' })};
  height: 3.75rem;
  padding: 0 0.75rem;
  gap: 1rem;

  ${({ theme, sticky }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};

    ${sticky &&
    css`
      position: sticky;
      top: 0;
    `}
  `};
`;

const LeftContent = styled.div`
  ${Flex({ items: 'center' })};
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
