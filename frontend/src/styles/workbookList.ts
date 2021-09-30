import { css } from '@emotion/react';

import { DEVICE } from '../constants';

const WorkbookListStyle = css`
  display: grid;
  grid-template-columns: repeat(1, minmax(15rem, 44.25rem));
  gap: 1rem;

  @media ${DEVICE.TABLET} {
    grid-template-columns: repeat(2, minmax(20rem, 22.25rem));
  }
`;

export default WorkbookListStyle;
