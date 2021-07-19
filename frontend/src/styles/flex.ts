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

const Flex = ({ direction, justify, items, wrap }: Props = {}) => css`
  display: flex;
  flex-direction: ${direction ?? 'row'};
  justify-content: ${justify ?? 'flex-start'};
  align-items: ${items ?? 'stretch'};
  flex-wrap: ${wrap ?? 'nowrap'};
`;

export default Flex;
