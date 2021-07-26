import React, { createContext, useState } from 'react';

interface Values {
  [key: string]: string;
}

interface ErrorMessages {
  [key: string]: string | null;
}

interface Props {
  initialValues: Values;
  validators?: {
    [key: string]: (value: string) => never | void;
  };
  onSubmit: () => void;
  children: React.ReactElement | React.ReactElement[];
}

interface FormContextType {
  values: Values;
  onChange: React.ChangeEventHandler<HTMLInputElement | HTMLTextAreaElement>;
  errorMessages: ErrorMessages;
}

export const FormContext = createContext<null | FormContextType>(null);

const FormProvider = ({
  initialValues,
  validators,
  onSubmit,
  children,
}: Props) => {
  const [values, setValues] = useState<Values>(initialValues);
  const [errorMessages, setErrorMessages] =
    useState<ErrorMessages>(initialValues);

  const onChange: React.ChangeEventHandler<HTMLInputElement> = ({ target }) => {
    const key = target.name;
    const value = target.value;
    const validator = validators?.[key];

    setValues({ ...values, [key]: value });

    try {
      validator?.(value);
      setErrorMessages({ ...errorMessages, [key]: null });
    } catch (error) {
      console.error(error);
      setErrorMessages({ ...errorMessages, [key]: error.message });
    }
  };

  const onSubmitForm: React.FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();

    const emptyInput = Object.keys(values).find((key) => values[key] === '');

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

    onSubmit();
  };

  return (
    <FormContext.Provider value={{ values, errorMessages, onChange }}>
      <form onSubmit={onSubmitForm}>{children}</form>
    </FormContext.Provider>
  );
};

export default FormProvider;
