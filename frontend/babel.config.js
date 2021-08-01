const isDevelopment = process.env.NODE_ENV !== 'production';

module.exports = {
  presets: [
    [
      '@babel/preset-env',
      {
        useBuiltIns: 'usage',
        corejs: 3,
      },
    ],
    '@babel/preset-react',
    '@babel/preset-typescript',
  ],
  plugins: [
    '@babel/proposal-class-properties',
    '@babel/proposal-object-rest-spread',
    '@emotion',
    isDevelopment && require.resolve('react-refresh/babel'),
  ].filter(Boolean),
};
