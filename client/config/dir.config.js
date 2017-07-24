const path = require('path');
const helpers = require('./helpers');
const PATHS = {
    src: helpers.root('src'),
    app: helpers.root('src', 'main'),
    vendor: helpers.root('src', 'vendor'),
    polyfill: helpers.root('src', 'polyfill'),
    build: helpers.root('dist'),
};

module.exports = PATHS;