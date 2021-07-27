import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

interface Props {
  editable?: boolean;
  isChecked?: boolean;
  className?: string;
  onClickEditButton?: React.MouseEventHandler<HTMLButtonElement>;
  onClick?: React.MouseEventHandler<HTMLDivElement>;
  children: React.ReactElement | React.ReactElement[];
}

interface ContainerStyleProps extends React.HTMLAttributes<HTMLDivElement> {
  isChecked: boolean;
}

const CardTemplate = ({
  editable = false,
  isChecked = false,
  className,
  onClickEditButton,
  onClick,
  children,
}: Props) => (
  <Container className={className} isChecked={isChecked} onClick={onClick}>
    {children}
    {editable && (
      <Footer>
        <button onClick={onClickEditButton}>수정</button>
        <button>삭제</button>
      </Footer>
    )}
  </Container>
);

const Container = styled.div<ContainerStyleProps>`
  padding: 1rem;
  word-break: break-all;

  ${({ theme, isChecked, onClick }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
    cursor: ${onClick ? 'pointer' : 'default'};
  `};
`;

const Footer = styled.div`
  margin-top: 1rem;
  text-align: right;

  ${({ theme }) => css`
    & > button:first-of-type {
      color: ${theme.color.gray_7};
    }
    & > button:last-of-type {
      color: ${theme.color.red};
    }
  `}

  & > button {
    margin-left: 1rem;
  }
`;

export default CardTemplate;
