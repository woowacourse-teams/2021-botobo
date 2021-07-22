module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  moduleNameMapper: {
    '\\.svg$': '<rootDir>/src/__mocks__/svgrMock.tsx',
  },
  setupFiles: ['dotenv/config', './src/__mocks__/browser.ts'],
  setupFilesAfterEnv: ['./src/__mocks__/setupTest.ts'],
};
