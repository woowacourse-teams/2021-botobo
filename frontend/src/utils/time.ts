export const timeConverter = (time: number) => {
  const seconds = time % 60;
  const minutes = Math.floor((time % 3600) / 60) % 60;
  const hours = Math.floor(time / 3600) % 24;

  return [
    hours < 10 ? `0${hours}` : `${hours}`,
    minutes < 10 ? `0${minutes}` : `${minutes}`,
    seconds < 10 ? `0${seconds}` : `${seconds}`,
  ];
};
