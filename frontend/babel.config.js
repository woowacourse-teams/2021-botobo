const isDevelopment = process.env.NODE_ENV !== 'production';
const isWeb = typeof window !== 'undefined';

module.exports = {
  presets: [
    [
      '@babel/preset-env',
      {
        useBuiltIns: 'usage',
        corejs: 3,
        targets: {
          browsers: ['> 1% in KR', 'not ie <= 10'],
        },
      },
    ],
    '@babel/preset-react',
    '@babel/preset-typescript',
  ],
  plugins: [
    '@babel/proposal-class-properties',
    '@babel/proposal-object-rest-spread',
    '@emotion',
    isDevelopment && isWeb && require.resolve('react-refresh/babel'),
  ].filter(Boolean),
};
