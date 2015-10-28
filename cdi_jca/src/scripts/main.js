var isWebKit = 'webkitAppearance' in document.documentElement.style;
var bespoke = require('bespoke'),
  keys = require('bespoke-keys'),
  touch = require('bespoke-touch'),
  bullets = require('bespoke-bullets'),
  classes = require('bespoke-classes'),
  scale = require('bespoke-scale'),
  backdrop = require('bespoke-backdrop'),
  overview = require('bespoke-overview'),
  onstage = require('bespoke-onstage'),
  hash = require('bespoke-hash'),
  nav = require('bespoke-nav'),
  pdf = require('bespoke-pdf');

bespoke.from('article.deck', [
  classes(),
  onstage(),
  nav(),
  overview(),
  bullets('ul:not(.no-bullets) li, ol:not(.no-bullets) li, .bullet'),
  scale(isWebKit ? 'zoom' : 'transform'),
  backdrop(),
  hash(),
  pdf()
]);

require('prism');
require('prism-clojure/prism.clojure.js');
