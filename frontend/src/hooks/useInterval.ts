import { useEffect } from 'react';

const useInterval = (
  handler: TimerHandler,
  timeout?: number,
  executionCondition = true
) => {
  useEffect(() => {
    if (!executionCondition) return;

    const id = window.setInterval(handler, timeout);

    return () => window.clearInterval(id);
  }, [timeout]);
};

export default useInterval;
