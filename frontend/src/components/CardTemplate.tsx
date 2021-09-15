import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { Link } from 'react-router-dom';

interface Props {
  editable?: boolean;
  isChecked?: boolean;
  className?: string;
  path?: string;
  onClickEditButton?: React.MouseEventHandler<HTMLButtonElement>;
  onClickDeleteButton?: React.MouseEventHandler<HTMLButtonElement>;
  onClick?: React.MouseEventHandler<HTMLDivElement>;
  children: React.ReactElement | React.ReactElement[];
}

interface ContainerStyleProps extends React.HTMLAttributes<HTMLDivElement> {
  isChecked: boolean;
  isClickable: boolean;
}

const CardTemplate = ({
  editable = false,
  isChecked = false,
  className,
  path,
  onClickEditButton,
  onClickDeleteButton,
  onClick,
  children,
}: Props) =>
  path ? (
    <LinkContainer to={path}>
      <Container className={className} isChecked={isChecked} isClickable={true}>
        {children}
        {editable && (
          <Footer>
            <button
              onClick={(event) => {
                event.preventDefault();

                onClickEditButton?.(event);
              }}
            >
              수정
            </button>
            <button
              onClick={(event) => {
                event.preventDefault();

                onClickDeleteButton?.(event);
              }}
            >
              삭제
            </button>
          </Footer>
        )}
      </Container>
    </LinkContainer>
  ) : (
    <Container
      className={className}
      isChecked={isChecked}
      onClick={onClick}
      isClickable={Boolean(onClick)}
    >
      {children}
      {editable && (
        <Footer>
          <button onClick={onClickEditButton}>수정</button>
          <button onClick={onClickDeleteButton}>삭제</button>
        </Footer>
      )}
    </Container>
  );

const LinkContainer = styled(Link)`
  cursor: pointer;
`;

const Container = styled.div<ContainerStyleProps>`
  padding: 1rem;
  word-break: break-all;
  white-space: pre-wrap;

  ${({ theme, isChecked, isClickable }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    -webkit-box-shadow: ${isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
    box-shadow: ${isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
    cursor: ${isClickable ? 'pointer' : 'default'};
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
