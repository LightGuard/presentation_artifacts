// Require Node modules in the browser thanks to Browserify: http://browserify.org
var bespoke = require('bespoke'),
  keys = require('bespoke-keys'),
  touch = require('bespoke-touch'),
  bullets = require('bespoke-bullets'),
  scale = require('bespoke-scale'),
  fx = require('bespoke-fx'),
  classes = require('bespoke-classes'),
  hash = require('bespoke-hash');

// Bespoke.js
window.slide_deck = bespoke.from('article', [
  classes(),
  bullets('li, .bullet'),
  keys(),
  touch(),
  scale(),
  hash(),
  //fx({
      //direction: "horizontal",
      //transition: "move",
      //reverse: false
     //}
    //)
]);

