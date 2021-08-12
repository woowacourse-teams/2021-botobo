import { Theme } from '@emotion/react';

const theme: Theme = {
  color: {
    white: '#ffffff',
    gray_0: '#f8f9fa',
    gray_1: '#f1f3f5',
    gray_2: '#e9ecef',
    gray_3: '#dee2e6',
    gray_4: '#ced4da',
    gray_5: '#adb5bd',
    gray_6: '#868e96',
    gray_7: '#495057',
    gray_8: '#343a40',
    gray_9: '#212529',
    black: '#000000',
    pink: '#f582c6',
    green: '#02c584',
    blue: '#0084F4',
    red: '#d32f2f',
  },
  boxShadow: {
    header: `0 2px 5px 0 rgb(0 0 0 / 12%)`,
    card: `rgba(60, 64, 67, 0.3) 0px 1px 2px 0px,
    rgba(60, 64, 67, 0.15) 0px 2px 6px 2px`,
    button: `rgba(99, 99, 99, 0.2) 0px 2px 8px 0px`,
    inset: '0 0 0 1.5px inset',
  },
  borderRadius: {
    circle: '50%',
    rectangle: '0',
    round: '500px',
    square: '8px',
    square_1: '12px',
    square_2: '16px',
    square_3: '20px',
    square_4: '24px',
    square_5: '28px',
    square_6: '32px',
    square_7: '36px',
    square_8: '40px',
    square_9: '44px',
  },
  fontSize: {
    small: '0.8rem',
    default: '1rem',
    medium: '1.2rem',
    semiLarge: '1.4rem',
    large: '1.6rem',
  },
  fontWeight: {
    normal: '400',
    semiBold: '500',
    bold: '700',
  },
  responsive: {
    maxWidth: '768px',
  },
};

export default theme;
