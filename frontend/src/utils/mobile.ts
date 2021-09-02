export const isMobile = (() => {
  const { userAgent, maxTouchPoints } = window.navigator;

  const isMac = /Mac OS/i.test(userAgent);

  if (isMac && maxTouchPoints > 0) return true;

  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini|Mobi|mobi/i.test(
    userAgent
  );
})();
