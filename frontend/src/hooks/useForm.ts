import { useContext } from 'react';

import { FormContext } from './../contexts';

const useForm = () => {
  const formContext = useContext(FormContext);

  if (formContext === null) {
    throw new ReferenceError('해당 컨텍스트가 존재하지 않습니다.');
  }

  return {
    values: formContext.values,
    errorMessages: formContext.errorMessages,
    onChange: formContext.onChange,
    onBlur: formContext.onBlur,
  };
};

export default useForm;
