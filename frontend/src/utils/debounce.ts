const debounce = (() => {
  let timeoutId = 0;

  return <TArgs extends unknown[]>(
    callback: (...args: TArgs) => void,
    delay: number
  ) => {
    if (timeoutId) {
      clearTimeout(timeoutId);
    }

    timeoutId = setTimeout(callback, delay);
  };
})();

export default debounce;
