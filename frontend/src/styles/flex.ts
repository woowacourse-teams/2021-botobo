import { css } from '@emotion/react';

interface Props {
  direction?: 'row' | 'column';
  justify?:
    | 'flex-start'
    | 'flex-end'
    | 'center'
    | 'space-between'
    | 'space-around'
    | 'space-evenly';
  items?: 'stretch' | 'center' | 'flex-start' | 'flex-end';
  wrap?: 'nowrap' | 'wrap';
}

const Flex = ({
  direction = 'row',
  justify = 'flex-start',
  items = 'stretch',
  wrap = 'nowrap',
}: Props) => css`
  display: flex;
  flex-direction: ${direction};
  justify-content: ${justify};
  align-items: ${items};
  flex-wrap: ${wrap};
`;

export default Flex;
