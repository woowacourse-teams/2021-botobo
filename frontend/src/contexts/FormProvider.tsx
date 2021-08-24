import styled from '@emotion/styled';
import React, { createContext, useState } from 'react';

interface Values {
  [key: string]: string;
}

interface ErrorMessages {
  [key: string]: string | null;
}

interface Props {
  initialValues: Values;
  keysWithNoValueAllowed?: string[];
  validators?: {
    [key: string]: (value: string) => never | void | Promise<never | void>;
  };
  onSubmit: (values: Values) => void;
  children: React.ReactElement | React.ReactElement[];
}

interface FormContextType {
  values: Values;
  onChange: React.ChangeEventHandler<HTMLInputElement | HTMLTextAreaElement>;
  onBlur: React.FocusEventHandler<HTMLInputElement | HTMLTextAreaElement>;
  errorMessages: ErrorMessages;
}

export const FormContext = createContext<null | FormContextType>(null);

const FormProvider = ({
  initialValues,
  keysWithNoValueAllowed = [],
  validators,
  onSubmit,
  children,
}: Props) => {
  const [values, setValues] = useState<Values>(initialValues);
  const [errorMessages, setErrorMessages] = useState<ErrorMessages>({});

  const onChange: React.ChangeEventHandler<HTMLInputElement> = ({ target }) => {
    const key = target.name;
    const value = target.value;

    setValues({ ...values, [key]: value });
    setErrorMessages({ ...errorMessages, [key]: null });
  };

  const onBlur: React.FocusEventHandler<HTMLInputElement> = async ({
    target,
  }) => {
    const key = target.name;
    const value = target.value;
    const validator = validators?.[key];

    if (!keysWithNoValueAllowed.includes(key) && value.trim().length === 0) {
      setErrorMessages({ ...errorMessages, [key]: '내용을 입력해 주세요.' });

      return;
    }

    try {
      await validator?.(value);
      setErrorMessages({ ...errorMessages, [key]: null });
    } catch (error) {
      console.error(error);
      setErrorMessages({ ...errorMessages, [key]: error.message });
    }
  };

  const onSubmitForm: React.FormEventHandler<HTMLFormElement> = async (
    event
  ) => {
    event.preventDefault();

    const emptyInput = Object.keys(values).find(
      (key) =>
        !keysWithNoValueAllowed.includes(key) && values[key].trim().length === 0
    );

    if (emptyInput) {
      event.currentTarget[emptyInput].focus();

      return;
    }

    const invalidInput = Object.keys(errorMessages).find((key) =>
      Boolean(errorMessages[key])
    );

    if (invalidInput) {
      event.currentTarget[invalidInput].focus();

      return;
    }

    try {
      for (const key of Object.keys(values)) {
        await validators?.[key](values[key]);
      }
    } catch (error) {
      console.error(error);

      return;
    }

    onSubmit(values);
  };

  return (
    <FormContext.Provider value={{ values, errorMessages, onChange, onBlur }}>
      <Form onSubmit={onSubmitForm}>{children}</Form>
    </FormContext.Provider>
  );
};

const Form = styled.form`
  width: 100%;
`;

export default FormProvider;
