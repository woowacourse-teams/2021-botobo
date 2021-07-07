import '@emotion/react';

declare module '@emotion/react' {
  export interface Theme {
    color: Readonly<Color>;
  }

  interface Color {
    white: string;
    gray_0: string;
    gray_1: string;
    gray_2: string;
    gray_3: string;
    gray_4: string;
    gray_5: string;
    gray_6: string;
    gray_7: string;
    gray_8: string;
    gray_9: string;
    black: string;
    pink: string;
    green: string;
    indigo: string;
  }
}
