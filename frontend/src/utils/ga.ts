window.dataLayer = window.dataLayer || [];

const gTag = (key: string, value: string | Date) => {
  window.dataLayer.push(key, value);
};

export default gTag;
