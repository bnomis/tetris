# tetris

Work in progress.

A demo [re-frame](https://github.com/Day8/re-frame) application.

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
