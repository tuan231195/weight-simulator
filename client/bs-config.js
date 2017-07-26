var proxyMiddleware = require('http-proxy-middleware');
module.exports = {
	'server': {
		'baseDir': 'dist',
		'routes': {
			'/node_modules': 'node_modules'
		},
		'middleware': {
			1: proxyMiddleware('/api', {
				target: 'http://localhost:8081',
				changeOrigin: true   // for vhosted sites, changes host header to match to target's host
			})
		}
	}
};
