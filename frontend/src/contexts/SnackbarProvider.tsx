import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { createContext, useEffect, useState } from 'react';

import CloseIcon from '../assets/cross-mark.svg';
import { useTimeout } from '../hooks';
import { Flex } from '../styles';
import { formatNewLine } from '../utils';

interface SnackbarInfo {
  message: string;
  type?: 'default' | 'error';
}

interface Props {
  children: React.ReactElement | React.ReactElement[];
}

interface ContainerStyleProps extends Required<Omit<SnackbarInfo, 'message'>> {
  isVisible: boolean;
}

interface SnackbarProps {
  snackbarInfo: SnackbarInfo;
}

type SnackbarContextType = null | (({ message, type }: SnackbarInfo) => void);

export const SnackbarContext = createContext<SnackbarContextType>(null);

const SNACKBAR_DISPLAY_TIME = 4000;

const SnackbarProvider = ({ children }: Props) => {
  const [snackbarInfo, setSnackbarInfo] = useState<SnackbarInfo>({
    message: '',
    type: 'default',
  });

  const showSnackbar = ({ message, type }: SnackbarInfo) => {
    setSnackbarInfo({ message, type });
  };

  return (
    <SnackbarContext.Provider value={showSnackbar}>
      {children}
      {snackbarInfo.message && <Snackbar snackbarInfo={snackbarInfo} />}
    </SnackbarContext.Provider>
  );
};

const Snackbar = ({ snackbarInfo }: SnackbarProps) => {
  const [isVisible, setIsVisible] = useState(false);
  const { message, type = 'default' } = snackbarInfo;

  useEffect(() => {
    setIsVisible(true);
  }, [snackbarInfo, setIsVisible]);

  useTimeout(() => setIsVisible(false), SNACKBAR_DISPLAY_TIME);

  return (
    <Container isVisible={isVisible} type={type}>
      {formatNewLine(message)}
      <StyledCloseIcon onClick={() => setIsVisible(false)} />
    </Container>
  );
};

const Container = styled.div<ContainerStyleProps>`
  ${Flex({ items: 'center', justify: 'space-between' })};
  position: fixed;
  bottom: -4rem;
  width: 95%;
  left: 50%;
  height: 2.5rem;
  padding: 1.5rem;
  transition: transform 300ms linear;

  ${({ theme, isVisible, type }) => css`
    color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    background-color: ${type === 'default'
      ? theme.color.gray_8
      : theme.color.red};
    box-shadow: ${theme.boxShadow.card};
    transform: ${isVisible ? 'translate(-50%, -5rem)' : 'translate(-50%, 0)'};
  `};
`;

const StyledCloseIcon = styled(CloseIcon)`
  width: 1rem;
  height: 1rem;
  vertical-align: middle;
  margin-left: 1rem;

  ${({ theme }) =>
    css`
      fill: ${theme.color.white};
    `}
`;

export default SnackbarProvider;
