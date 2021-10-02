import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { WorkbookListStyle } from '../styles';
import CardSkeleton from './CardSkeleton';
import QnACardSkeleton from './QnACardSkeleton';

type CardType = 'workbook' | 'QnA';

interface Props {
  count: number;
  type?: CardType;
  hasHeader?: boolean;
  hasAuthor?: boolean;
  hasTag?: boolean;
  hasFooter?: boolean;
}

interface UlStyleProps {
  isResponsive: boolean;
}

const CardSkeletonList = ({
  count,
  type = 'workbook',
  hasHeader,
  hasAuthor,
  hasTag,
  hasFooter,
}: Props) => (
  <StyledUl isResponsive={type === 'workbook'}>
    {[...Array(count)].map((_, index) => (
      <li key={index}>
        {type === 'workbook' && (
          <CardSkeleton
            hasAuthor={hasAuthor}
            hasTag={hasTag}
            hasFooter={hasFooter}
          />
        )}
        {type === 'QnA' && (
          <QnACardSkeleton hasHeader={hasHeader} hasFooter={hasFooter} />
        )}
      </li>
    ))}
  </StyledUl>
);

const StyledUl = styled.ul<UlStyleProps>`
  margin: 1rem 0;

  ${({ isResponsive }) =>
    isResponsive
      ? WorkbookListStyle
      : css`
          display: grid;
          gap: 1rem;
        `}
`;

export default CardSkeletonList;
