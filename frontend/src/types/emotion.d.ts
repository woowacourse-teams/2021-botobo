import '@emotion/react';

declare module '@emotion/react' {
  export interface Theme {
    color: Readonly<Color>;
    boxShadow: Readonly<BoxShadow>;
    borderRadius: Readonly<BorderRadius>;
    fontSize: Readonly<FontSize>;
    fontWeight: Readonly<FontWeight>;
    responsive: Readonly<Responsive>;
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
    blue: string;
    red: string;
  }

  interface BoxShadow {
    header: string;
    card: string;
    button: string;
    inset: string;
  }

  interface BorderRadius {
    circle: string;
    rectangle: string;
    round: string;
    square: string;
    square_1: string;
    square_2: string;
    square_3: string;
    square_4: string;
    square_5: string;
    square_6: string;
    square_7: string;
    square_8: string;
    square_9: string;
  }

  interface FontSize {
    small: string;
    default: string;
    medium: string;
    semiLarge: string;
    large: string;
  }

  interface FontWeight {
    normal: string;
    semiBold: string;
    bold: string;
  }

  interface Responsive {
    maxWidth: string;
  }
}
