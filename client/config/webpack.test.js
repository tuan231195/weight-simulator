const webpack = require('webpack');
const merge = require('webpack-merge');
const helpers = require('./helpers');
const parts = require('./webpack.parts');
const ENV = process.env.NODE_ENV = process.env.ENV = 'test';

module.exports = merge([
	{
		devtool: 'inline-source-map',
		resolve: {
			extensions: [ '.ts', '.tsx', '.js' ]
		},
		plugins: [
			new webpack.ContextReplacementPlugin(
				// The (\\|\/) piece accounts for path separators in *nix and Windows
				/angular([\\\/])core([\\\/])@angular/,
				helpers.root('./src'), // location of your src
				{} // a map of your routes
			)
		]
	},
	parts.loadTypeScript(),
	parts.loadHTML(),
	parts.loadImages({
		loader: 'null-loader'
	}),
	parts.define({
		'process.env': {
			'ENV': JSON.stringify(ENV)
		}
	}),
	parts.loadFonts({
		loader: 'null-loader'
	}),
	parts.loadCSS({
		exclude: helpers.root('src', 'app'),
		use: 'null-loader'
	}),
	parts.loadCSS({
		include: helpers.root('src', 'app'),
		use: 'raw-loader'
	})
]);

