const HtmlWebpackHtml = require('html-webpack-plugin');
const cssnano = require('cssnano');
const webpack = require('webpack');
const PurifyCSSPlugin = require('purifycss-webpack');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const BabiliPlugin = require('babili-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const path = require('path');
const PATHS = require('./dir.config');
const helpers = require('./helpers');

exports.baseConfig = () => ({
    entry: {
        polyfill: PATHS.polyfill,
        vendor: PATHS.vendor,
        app: PATHS.app
    },
    output: {
        path: PATHS.build,
        chunkFilename: '[name].[chunkhash:8].js',
        filename: '[name].[chunkhash:8].js'
    },
    resolve: {
        // Add `.ts` and `.tsx` as a resolvable extension.
        extensions: ['.ts', '.tsx', '.js'], // note if using webpack 1 you'd also need a '' in the array as well,
        alias: {
            stompjs$: helpers.root('src', 'typings','stompjs.js')
        }
    }, plugins: [
        new webpack.ContextReplacementPlugin(
            // The (\\|\/) piece accounts for path separators in *nix and Windows
            /angular([\\\/])core([\\\/])@angular/,
            helpers.root('./src'), // location of your src
            {} // a map of your routes
        )
    ]
});

exports.devServer = ({host, port} = {}) => ({
    devServer: {
        publicPath: '/',
        historyApiFallback: true,
        stats: 'minimal',
        host, // Defaults to `localhost`
        port, // Defaults to 8080
        overlay: {
            errors: true,
            warnings: true
        }
    }
});

exports.loadSass = () => ({
	module: {
		rules: [{
			test: /\.scss$/,
			use: [{
				loader: "style-loader" // creates style nodes from JS strings
			}, {
				loader: "css-loader" // translates CSS into CommonJS
			}, {
				loader: "sass-loader" // compiles Sass to CSS
			}]
		}]
	}
});

exports.lintJavaScript = ({ include, exclude, options }) => ({
	module: {
		rules: [
			{
				test: /\.js$/,
				include,
				exclude,
				enforce: 'pre',

				loader: 'eslint-loader',
				options
			}
		]
	}
});


exports.loadJavaScript = ({ include, exclude }) => ({
	module: {
		rules: [
			{
				test: /\.js$/,
				include,
				exclude,
                loader: 'babel-loader',
                options: {
                    // Enable caching for improved performance during
                    // development.
                    // It uses default OS directory by default. If you need
                    // something more custom, pass a path to it.
                    // I.e., { cacheDirectory: '<path>' }
                    cacheDirectory: true
                }
            }
        ]
    }
});


exports.loadCSS = ({ include, exclude, use } = {}) => ({
	module: {
		rules: [
			{
				test: /\.css$/,
				include,
				exclude,
				use
			}
		]
	}
});

exports.extractCSS = ({include, exclude, use}) => {
    // Output extracted CSS to a file
    const plugin = new ExtractTextPlugin({
        filename: '[name].[contenthash:8].css'
    });

	return {
		module: {
			rules: [
				{
					test: /\.css$/,
					include,
					exclude,
                    use: plugin.extract({
                        use,
                        fallback: 'style-loader'
                    })
                }
            ]
        },
        plugins: [plugin]
    };
};

exports.autoprefix = () => ({
    loader: 'postcss-loader',
    options: {
        plugins: () => ([
            require('autoprefixer')()
        ])
    }
});

exports.purifyCSS = ({paths}) => ({
    plugins: [
        new PurifyCSSPlugin({paths})
    ]
});
exports.define = (variables) => ({
    plugins: [
        new webpack.DefinePlugin(variables)
    ]
});

exports.lintCSS = ({ include, exclude }) => ({
	module: {
		rules: [
			{
				test: /\.css$/,
				include,
				exclude,
				enforce: 'pre',
				loader: 'postcss-loader',
				options: {
					plugins: () => ([
						require('stylelint')()
					])
				}
			}
		]
	}
});

exports.loadImages = ({ include, exclude, options, loader } = {}) => ({
	module: {
		rules: [
			{
				test: /\.(png|jpg|svg)$/,
				include,
				exclude,
				use: {
					loader,
					options
				}
			}
		]
	}
});

exports.loadFonts = ({ include, exclude, loader, options } = {}) => ({
	module: {
		rules: [
			{
				// Capture eot, ttf, woff, and woff2
				test: /\.(eot|ttf|woff|woff2)(\?v=\d+\.\d+\.\d+)?$/,
				include,
				exclude,
                use: {
                    loader,
                    options
                }
            }
        ]
    }
});

exports.generateSourceMaps = ({type}) => ({
    devtool: type
});

exports.extractBundles = (bundles) => ({
    plugins: bundles.map((bundle) => (
        new webpack.optimize.CommonsChunkPlugin(bundle)
    ))
});

exports.clean = (path) => ({
    plugins: [
        new CleanWebpackPlugin([path], {
            root: process.cwd()
        })
    ]
});


exports.minifyJavaScript = () => ({
    plugins: [
        new BabiliPlugin()
    ]
});

exports.minifyCSS = ({options}) => ({
    plugins: [
        new OptimizeCSSAssetsPlugin({
            cssProcessor: cssnano,
            cssProcessorOptions: options,
            canPrint: false
        })
    ]
});

exports.extractHTML = (templatePath) => ({
    plugins: [
        new HtmlWebpackHtml({
            template: templatePath
        })
    ]
});

exports.loadTypeScript = () => ({
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: ['awesome-typescript-loader', 'angular2-template-loader']
            }
        ]
    }
});

exports.lintTypeScript = () => ({
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                enforce: 'pre',
                loader: 'tslint-loader',
                exclude: helpers.root('node_modules'),
                options: {
                    emitErrors: false,
                    failOnHint: true
                }
            }
        ]
    }
});
exports.loadHTML = () => ({
	module: {
		rules: [
			{
				test: /\.html$/,
				loader: 'html-loader'
			}
		]
	}
});

exports.ignoreErrors = () => ({
	plugins: [ new webpack.NoEmitOnErrorsPlugin() ]
});