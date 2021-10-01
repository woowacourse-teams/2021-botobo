import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeletonList, HeaderSkeleton } from '../components';
import { loadContent } from '../styles';
import PageTemplate from './PageTemplate';

const QuizSettingLoadable = () => (
  <>
    <HeaderSkeleton />
    <PageTemplate isScroll={true}>
      <Title />
      <Description />
      <CardSkeletonWrapper>
        <CardSkeletonList count={4} />
      </CardSkeletonWrapper>
      <Description />
      <SelectBoxSkeleton />
      <Description />
      <StyleButton size="full" shape="rectangle" backgroundColor={'gray_4'}>
        {' '}
      </StyleButton>
    </PageTemplate>
  </>
);

const Title = styled.div`
  height: 2rem;
  width: 50%;
  margin-bottom: 1rem;

  ${loadContent}
`;

const Description = styled.div`
  height: 1rem;
  width: 50%;

  ${loadContent}
`;

const SelectBoxSkeleton = styled.div`
  height: 3.25rem;
  width: 100%;
  margin-top: 1rem;
  margin-bottom: 3.5rem;

  ${loadContent}
`;

const CardSkeletonWrapper = styled.div`
  margin-top: 1rem;
  margin-bottom: 2.5rem;
`;

const StyleButton = styled(Button)`
  margin-top: 3.5rem;
`;

export default QuizSettingLoadable;
