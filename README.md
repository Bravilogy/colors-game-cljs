# Colours Game [![Build Status](https://travis-ci.org/Bravilogy/colours-game-clojure.svg?branch=master)](https://travis-ci.org/Bravilogy/colours-game-clojure)

## Development Mode

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```

## See it in action
https://frequent-scarecrow.surge.sh
