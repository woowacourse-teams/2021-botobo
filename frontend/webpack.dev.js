/* eslint-disable @typescript-eslint/no-var-requires */
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    port: 3000,
    open: true,
    compress: true,
    overlay: {
      warnings: true,
      errors: true,
    },
    historyApiFallback: true,
    hot: true,
  },
  plugins: [new ForkTsCheckerWebpackPlugin(), new ReactRefreshWebpackPlugin()],
});
