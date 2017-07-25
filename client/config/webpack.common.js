const path = require('path');
const webpack = require('webpack');
const parts = require('./webpack.parts');
const merge = require('webpack-merge');
const PATHS = require('./dir.config');
const helpers = require('./helpers');

const commonConfig = merge([
	parts.baseConfig(),
	parts.loadCSS({
		include: helpers.root('src', 'app'),
		use: 'raw-loader'
	}),
	parts.lintCSS({ include: PATHS.app }),
	parts.lintTypeScript(),
	parts.loadImages({
		loader: 'url-loader',
		options: {
			limit: 15000,
			name: '[name].[hash:8].[ext]'
		}
	}),
	parts.loadFonts({
		loader: 'file-loader',
		options: {
			name: '[name].[hash:8].[ext]'
		}
	}),
	parts.loadTypeScript(),
	parts.loadHTML(),

	parts.extractBundles([
		{
			name: [ 'app', 'vendor', 'polyfill' ]
		},
		{
			name: 'manifest',
			minChunks: Infinity
		}
	]),
	parts.extractHTML(path.resolve(PATHS.src, 'index.html')),
	{
		node: {
			net: 'empty',
			tls: 'empty'
		}
	}
]);

module.exports = commonConfig;