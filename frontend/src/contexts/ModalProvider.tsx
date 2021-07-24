import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React, { createContext, useState } from 'react';

import CloseButton from '../assets/cross-mark.svg';
import { theme } from '../constants';

type ModalType = 'center' | 'bottom';

interface Props {
  children: React.ReactElement;
}

interface BottomSheetProps {
  children: React.ReactElement[];
  type: ModalType;
  isOpened: boolean;
}

interface ModalContextType {
  openModal: (
    modalComponent: React.ReactElement,
    modalType?: ModalType
  ) => void;
  closeModal: () => void;
}

const modalStyle = {
  center: css`
    padding: 1rem;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    border-radius: ${theme.borderRadius.square_4};
  `,
  bottom: css`
    bottom: 0;
    left: 0;
    right: 0;
    border-top-left-radius: ${theme.borderRadius.square_6};
    border-top-right-radius: ${theme.borderRadius.square_6};
  `,
};

export const ModalContext = createContext<null | ModalContextType>(null);

const ModalProvider = ({ children }: Props) => {
  const [modal, setModal] = useState<null | React.ReactElement>(null);
  const [type, setType] = useState<ModalType>('bottom');
  const [isOpened, setIsOpened] = useState(false);

  const openModal = (
    modalComponent: React.ReactElement,
    modalType?: ModalType
  ) => {
    setModal(modalComponent);
    setType(modalType ?? 'bottom');
    setIsOpened(true);
  };

  const closeModal = () => {
    setIsOpened(false);
  };

  const closeWithDimmed: React.MouseEventHandler<HTMLDivElement> = ({
    target,
    currentTarget,
  }) => {
    if (currentTarget !== target) return;

    closeModal();
  };

  return (
    <ModalContext.Provider value={{ openModal, closeModal }}>
      {isOpened && <Dimmed onClick={closeWithDimmed} />}
      {modal && (
        <Modal isOpened={isOpened} type={type}>
          <Header>
            <CloseButton width="0.8rem" height="0.8rem" onClick={closeModal} />
          </Header>
          {modal}
        </Modal>
      )}
      {children}
    </ModalContext.Provider>
  );
};

const Modal = ({ children, type, isOpened }: BottomSheetProps) => (
  <Container isOpened={isOpened} type={type}>
    {children}
  </Container>
);

const fadeInAnimation = (type: ModalType) => keyframes`
  from { 
    ${type === 'bottom' ? 'transform: translateY(100%);' : 'opacity: 0;'} 
  } 
  to { 
    ${type === 'bottom' ? 'transform: translateY(0);' : 'opacity: 1;'} 
  }
`;

const fadeOutAnimation = (type: ModalType) => keyframes`
  from { 
    ${type === 'bottom' ? 'transform: translateY(0);' : 'opacity: 1;'}   
  } 
  to { 
    ${type === 'bottom' ? 'transform: translateY(100%);' : 'opacity: 0;'} 
  }
`;

const Dimmed = styled.div`
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;

  ${({ theme }) => css`
    background-color: ${`${theme.color.black}1A`};
  `};
`;

const Header = styled.div`
  text-align: right;
  padding-right: 0.5rem;
  margin-bottom: 0.5rem;
`;

const Container = styled.div<Pick<BottomSheetProps, 'type' | 'isOpened'>>`
  position: fixed;
  z-index: 1;
  padding: 1rem;

  transition: visibility 0.2s;

  ${({ theme, type, isOpened }) => css`
    background-color: ${theme.color.white};
    animation: ${isOpened ? fadeInAnimation(type) : fadeOutAnimation(type)} 0.2s;
    visibility: ${isOpened ? 'visible' : 'hidden'};

    ${modalStyle[type]}
  `}
`;

export default ModalProvider;
