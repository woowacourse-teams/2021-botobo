/* eslint-disable @typescript-eslint/no-var-requires */
const path = require('path');
const nodeExternals = require('webpack-node-externals');
const Dotenv = require('dotenv-webpack');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
  entry: './server/index.js',

  target: 'node',

  externals: [nodeExternals()],

  output: {
    path: path.resolve(__dirname, 'dist-server'),
    filename: 'index.js',
    chunkFilename: '[name].[chunkhash].js',
    clean: true,
  },

  plugins: [
    new Dotenv(),
    new CopyPlugin({
      patterns: [
        {
          from: './package.json',
          to: path.resolve(__dirname, 'dist-server'),
        },
      ],
    }),
  ],

  module: {
    rules: [
      {
        test: /\.(js|ts)x?$/,
        use: {
          loader: 'babel-loader',
        },
      },
      {
        test: /\.svg$/,
        use: [
          {
            loader: '@svgr/webpack',
            options: {
              prettier: false,
            },
          },
        ],
        issuer: {
          and: [/\.(js|ts)x?$/],
        },
      },
    ],
  },

  resolve: {
    extensions: ['.tsx', '.ts', '.jsx', '.js'],
  },
};
