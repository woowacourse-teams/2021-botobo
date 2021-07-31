import { css } from '@emotion/react';

import { theme } from '../constants';

const scrollBarStyle = css`
  ::-webkit-scrollbar {
    width: 2px;
  }
  ::-webkit-scrollbar-thumb {
    background-color: ${theme.color.gray_4};
  }
`;

export default scrollBarStyle;
