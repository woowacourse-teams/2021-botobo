import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React, { createContext, useState } from 'react';

import BackIcon from '../assets/chevron-left-solid.svg';
import CloseButton from '../assets/cross-mark.svg';
import { theme } from '../constants';
import { Flex } from '../styles';

type ModalType = 'center' | 'bottom' | 'full';

interface Props {
  children: React.ReactElement | React.ReactElement[];
}

interface BottomSheetProps {
  children: React.ReactElement | React.ReactElement[];
  type: ModalType;
  isOpened: boolean;
}
interface ModalInfo {
  content: React.ReactElement;
  title?: string;
  type?: ModalType;
}

interface ModalContextType {
  openModal: ({ content, title, type }: ModalInfo) => void;
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
  full: css`
    height: 100%;
    overflow-y: auto;
    left: 0;
    right: 0;
  `,
};

export const ModalContext = createContext<null | ModalContextType>(null);

const ModalProvider = ({ children }: Props) => {
  const [modalContent, setModalContent] = useState<null | React.ReactElement>(
    null
  );
  const [title, setTitle] = useState('');
  const [type, setType] = useState<ModalType>('bottom');
  const [isOpened, setIsOpened] = useState(false);

  const openModal = ({ content, title, type }: ModalInfo) => {
    setModalContent(content);
    setTitle(title ?? '');
    setType(type ?? 'bottom');
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
      {modalContent && (
        <Modal isOpened={isOpened} type={type}>
          <Header>
            {type === 'full' ? (
              <BackIcon width="1.2rem" height="1.2rem" onClick={closeModal} />
            ) : (
              <CloseButton
                width="0.8rem"
                height="0.8rem"
                onClick={closeModal}
              />
            )}
            <h2>{title}</h2>
          </Header>
          {modalContent}
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
    ${type === 'center' ? 'opacity: 0;' : 'transform: translateY(100%);'} 
  } 
  to { 
    ${type === 'center' ? 'opacity: 1;' : 'transform: translateY(0);'} 
  }
`;

const fadeOutAnimation = (type: ModalType) => keyframes`
  from { 
    ${type === 'center' ? 'opacity: 1;' : 'transform: translateY(0);'}   
  } 
  to { 
    ${type === 'center' ? 'opacity: 0;' : 'transform: translateY(100%);'} 
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
  ${Flex({ items: 'center', justify: 'center' })};
  position: relative;
  margin-top: 0.5rem;
  margin-bottom: 2rem;

  & > svg {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    left: 0;
  }
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
