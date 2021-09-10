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
          browsers: [
            'Chrome >= 60',
            'Safari >= 10.1',
            'iOS >= 10.3',
            'Firefox >= 54',
            'Edge >= 15',
            'samsung >= 5',
          ],
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
