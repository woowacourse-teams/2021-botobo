export const timeConverter = (time: number) => {
  const seconds = time % 60;
  const minutes = Math.floor((time % 3600) / 60) % 60;
  const hours = Math.floor(time / 3600) % 24;

  return `${time >= 3600 ? `${hours}시간` : ''} ${
    time >= 60 ? `${minutes}분` : ''
  } ${`${seconds}초`}`;
};
