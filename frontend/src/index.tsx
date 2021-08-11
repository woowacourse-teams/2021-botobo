import React from 'react';
import ReactDOM from 'react-dom';

import App from './App';
import { gTag } from './utils';

const startGA = () => {
  const code = process.env.REACT_APP_GA_CODE;

  if (!code) return;

  gTag('js', new Date());
  gTag('config', code);
};

startGA();

ReactDOM.render(<App />, document.getElementById('root'));
