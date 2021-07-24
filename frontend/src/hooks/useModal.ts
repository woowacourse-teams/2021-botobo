import { useContext } from 'react';

import { ModalContext } from '../contexts';

const useModal = () => {
  const modalContext = useContext(ModalContext);

  if (modalContext === null) {
    throw new ReferenceError('해당 컨텍스트가 존재하지 않습니다.');
  }

  return {
    openModal: modalContext.openModal,
    closeModal: modalContext.closeModal,
  };
};

export default useModal;
