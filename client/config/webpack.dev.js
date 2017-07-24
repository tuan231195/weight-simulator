const commonConfig = require('./webpack.common');
const parts = require('./webpack.parts.js');
const merge = require('webpack-merge');
const webpack = require('webpack');
const helpers = require("./helpers");
const ENV = process.env.NODE_ENV = process.env.ENV = 'development';
const devConfig = merge([
    parts.generateSourceMaps({
        type: 'cheap-module-eval-source-map'
    }), 
    parts.define({
        'process.env': {
            'ENV': JSON.stringify(ENV)
        }
    }),
    parts.loadCSS({
        exclude: helpers.root('src', 'app'),
        use: ['style-loader', 'css-loader']
    }),
    parts.devServer({
        host: process.env.HOST, // Defaults to `localhost`
        port: process.env.PORT // Defaults to 8080
    })
]);

module.exports = merge([commonConfig, devConfig]);