import React from 'react';

const formatNewLine = (data: string) => (
  <span>
    {data.split('\n').map((line, index) => (
      <span key={index}>
        {line}
        <br />
      </span>
    ))}
  </span>
);

export default formatNewLine;
