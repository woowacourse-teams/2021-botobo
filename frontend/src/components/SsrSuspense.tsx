import React, { Suspense } from 'react';

interface Props {
  fallback: React.ReactElement | null;
  children: React.ReactNode;
}

const SsrSuspense = ({ fallback, children }: Props) =>
  typeof window === 'undefined' ? (
    <>{children}</>
  ) : (
    <Suspense fallback={fallback}>{children}</Suspense>
  );

export default SsrSuspense;
