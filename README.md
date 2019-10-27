# tetris

A work in progress.

A demo [re-frame](https://github.com/Day8/re-frame) application.

Uses [re-frame-10x](https://github.com/Day8/re-frame-10x) to expose the application state.

## Some Things To Try

Pause the game go backwards in time. Look at the application state.

## Requirements

[Clojure](https://clojure.org/)

[ClojureScript](https://clojurescript.org/)

[Shadow CLJS](http://shadow-cljs.org/)

[Leiningen](https://leiningen.org/)

## Development Mode

### Compile css

Compile css file once.

```shell
lein garden once
```

Automatically recompile css file on change.

```shell
lein garden auto
```

### Run application

```shell
lein clean
lein dev
```

shadow-cljs will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:8280](http://localhost:8280).

## References

<https://en.wikipedia.org/wiki/Tetris>
<https://tetris.fandom.com/wiki/Tetris_Guideline>
