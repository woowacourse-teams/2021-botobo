import styled from '@emotion/styled';
import React from 'react';

import useModal from '../hooks/useModal';

interface Props {
  children: React.ReactElement | React.ReactElement[] | string;
  onConfirm: (() => void) | (() => Promise<void>);
}

const Confirm = ({ children, onConfirm }: Props) => {
  const { closeModal } = useModal();

  return (
    <Container>
      <Message>{children}</Message>
      <ButtonWrapper>
        <button onClick={closeModal}>취소</button>
        <button
          onClick={() => {
            onConfirm();
            closeModal();
          }}
        >
          확인
        </button>
      </ButtonWrapper>
    </Container>
  );
};

const Container = styled.div`
  width: 18.75rem;
  min-height: 5rem;
  position: relative;
`;

const Message = styled.div`
  margin-top: 1rem;
`;

const ButtonWrapper = styled.div`
  position: absolute;
  bottom: 0;
  right: 0;
  margin-right: 0.5rem;

  & > button {
    margin-left: 2rem;
  }
`;

export default Confirm;
