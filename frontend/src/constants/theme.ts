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
    pink: '#ff73c9',
    green: '#00d344',
    indigo: '#29386b',
  },
  boxShadow: {
    card: `rgba(60, 64, 67, 0.3) 0px 1px 2px 0px,
    rgba(60, 64, 67, 0.15) 0px 2px 6px 2px;`,
    button: `rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;`,
  },
  borderRadius: {
    circle: '50%',
    square_1: '8px',
    square_2: '12px',
  },
  fontSize: {
    default: '1rem',
    medium: '1.2rem',
    semiLarge: '1.4rem',
    large: '1.6rem',
  },
};

export default theme;
