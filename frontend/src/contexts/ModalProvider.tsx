import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React, { createContext, useEffect, useState } from 'react';

import BackIcon from '../assets/chevron-left-solid.svg';
import CloseButtonIcon from '../assets/cross-mark.svg';
import DownIcon from '../assets/down-arrow.svg';
import { theme } from '../constants';
import { Flex } from '../styles';

type ModalType = 'center' | 'bottom' | 'full';

type CloseIconType = '' | 'back' | 'crossMark' | 'down';

interface Props {
  children: React.ReactElement | React.ReactElement[];
}

interface BottomSheetProps {
  children: React.ReactNode;
  type: ModalType;
  isOpened: boolean;
  deleteContent: () => void;
}

interface HeaderStyleProps {
  closeIcon: CloseIconType;
}
interface ModalInfo {
  content: React.ReactElement;
  title?: string;
  closeIcon?: CloseIconType;
  type?: ModalType;
}

interface ModalContextType {
  openModal: ({ content, title, closeIcon, type }: ModalInfo) => void;
  closeModal: () => void;
}

const modalStyle = {
  center: css`
    padding: 1rem;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    border-radius: ${theme.borderRadius.square_1};
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

export const ModalContext = createContext<ModalContextType | null>(null);

const ModalProvider = ({ children }: Props) => {
  const [modalContent, setModalContent] = useState<React.ReactElement | null>(
    null
  );
  const [title, setTitle] = useState('');
  const [closeIcon, setCloseIcon] = useState<CloseIconType>('');
  const [type, setType] = useState<ModalType>('bottom');
  const [isOpened, setIsOpened] = useState(false);

  const openModal = ({ content, title, closeIcon, type }: ModalInfo) => {
    setModalContent(content);
    setTitle(title ?? '');
    setCloseIcon(closeIcon ?? '');
    setType(type ?? 'bottom');
    setIsOpened(true);
  };

  const closeModal = () => {
    setIsOpened(false);
  };

  const deleteContent = () => {
    setModalContent(null);
  };

  const closeWithDimmed: React.MouseEventHandler<HTMLDivElement> = ({
    target,
    currentTarget,
  }) => {
    if (currentTarget !== target) return;

    closeModal();
  };

  useEffect(() => {
    const reset = () => {
      closeModal();
      deleteContent();
    };

    window.addEventListener('popstate', reset);

    return () => window.removeEventListener('popstate', reset);
  }, []);

  return (
    <ModalContext.Provider value={{ openModal, closeModal }}>
      {isOpened && <Dimmed onClick={closeWithDimmed} />}
      <Modal isOpened={isOpened} type={type} deleteContent={deleteContent}>
        {(closeIcon || title) && (
          <Header closeIcon={closeIcon}>
            <button type="button" onClick={closeModal}>
              {closeIcon === 'back' && (
                <BackIcon width="1.2rem" height="1.2rem" />
              )}
              {closeIcon === 'crossMark' && (
                <CloseButtonIcon width="0.8rem" height="0.8rem" />
              )}
              {closeIcon === 'down' && (
                <DownIcon width="1.2rem" height="1.2rem" />
              )}
            </button>
            <h2>{title}</h2>
          </Header>
        )}
        {modalContent}
      </Modal>
      {children}
    </ModalContext.Provider>
  );
};

const Modal = ({
  children,
  type,
  isOpened,
  deleteContent,
}: BottomSheetProps) => (
  <Container
    isOpened={isOpened}
    type={type}
    onAnimationEnd={() => {
      if (isOpened) return;

      deleteContent();
    }}
  >
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
  z-index: 1;

  ${({ theme }) => css`
    background-color: ${`${theme.color.black}80`};
  `};
`;

const Header = styled.div<HeaderStyleProps>`
  ${Flex({ items: 'center', justify: 'center' })};
  position: relative;
  margin-top: 0.5rem;
  margin-bottom: 2rem;

  & > button {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    left: 0;

    ${({ theme, closeIcon }) => css`
      width: ${closeIcon === 'down' ? '100%' : ''};

      & > svg {
        fill: ${theme.color.gray_8};
      }
    `}
  }
`;

const Container = styled.div<Pick<BottomSheetProps, 'type' | 'isOpened'>>`
  position: fixed;
  z-index: 2;
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
