# docker-clojure

This is the repository for the [official Docker image for Clojure](https://registry.hub.docker.com/_/clojure/).
It is automatically pulled and built by Stackbrew into the Docker registry.
This image runs on OpenJDK 8, 11, 14, 15, and 16 and includes [Leiningen](http://leiningen.org),
[boot](http://boot-clj.com), and/or [tools-deps](https://clojure.org/reference/deps_and_cli)
(see below for tags and building instructions).

## Leiningen vs. boot vs. tools-deps

The version tags on these images look like `(openjdk-major-version-)lein-N.N.N(-distro)`,
`(openjdk-major-version-)boot-N.N.N(-distro)`, and `(openjdk-major-version-)tools-deps(-distro)`.
These refer to which version of leiningen, boot, or tools-deps is packaged in the image (because they can then install
and use any version of Clojure at runtime). The `lein` (or `lein-slim-buster`, `openjdk-14-lein`, etc.)
images will always have a recent version of leiningen installed. If you want boot, specify either `clojure:boot`,
`clojure:boot-slim-buster`, or `clojure:boot-N.N.N`, `clojure:boot-N.N.N-slim-buster`,
`clojure:openjdk-14-boot-N.N.N-slim-buster`, etc. (where `N.N.N` is the version of boot you want installed). If
you want to use tools-deps, specify either `clojure:tools-deps`, `clojure:tools-deps-slim-buster` or other similar
variants.

### Note about the latest tag

As of 2020-3-20 the `clojure:latest` (also `clojure` because `latest` is the default) now has leiningen, boot, and
tools-deps installed.

Previously this tag only had leiningen installed. Installing the others is helpful for quick start examples, newcomers,
etc. as leiningen is by no means the de facto standard build tool these days. The downside is that the image is larger.
But for the `latest` tag it's a good trade off because for anything real we have always recommended using more specific
tags. No other tags are affected by this change.

## JDK versions

Java has recently introduced a new release cadence of every 6 months and dropped the leading `1` major version number.
As of 2019-9-25, our images will default to the latest LTS release of OpenJDK (currently 11). But we also now provide
the ability to specify which version of Java you'd like via Docker tags:

JDK 1.8 tools-deps image: `clojure:openjdk-8-tools-deps`
JDK 11 variant of that image: `clojure:openjdk-11-tools-deps` or `clojure:tool-deps`
JDK 14 with the latest release of leiningen: `clojure:openjdk-14`
JDK 15 with boot 2.8.3: `clojure:openjdk-15-boot-2.8.3`

## Linux distro

The upstream OpenJDK images are built on a few different variants of Debian Linux, so we have exposed those in our
Docker tags as well. The default is now Debian slim-buster. But you can also specify which distro you'd like by
appending it to the end of your Docker tag as in the following examples (but note that not every combination is
provided upstream and thus likewise for us):

JDK 1.8 leiningen on Debian slim-buster: `clojure:openjdk-8` or `clojure:openjdk-8-lein` or `clojure:openjdk-8-lein-stretch`
JDK 1.8 leiningen on Debian buster: `clojure:openjdk-8-buster` or `clojure:openjdk-8-lein-buster`
JDK 11 tools-deps on Debian slim-buster: `clojure:tools-deps` or `clojure:openjdk-11-tools-deps` or `clojure:openjdk-11-tools-deps-slim-buster`
JDK 15 tools-deps on Alpine: `clojure:openjdk-15-tools-deps-alpine`

## Alpine Linux

Most of the upstream alpine-based openjdk builds have been deprecated, so we have followed suit. As of 2020-8-3 we
provide an alpine variant for OpenJDK 15 and 16 builds, but that's it. And it is likely that that build will go away once
OpenJDK 15 is released (as has happened with other recent releases).

For other versions of OpenJDK, we recommend migrating to the `slim-buster` variant instead. The older `alpine` images
won't go away, but neither will they receive security updates, version bumps, etc. We recommend that you cease using
them until / unless official upstream support resumes.

### `clojure:slim-buster`

These images are based on the Debian buster distribution but have fewer packages installed and are thus much smaller
than the `stretch` or `buster` images. Their use is recommended.

## Examples

### Interactive Shell

Run an interactive shell from this image.

```
docker run -i -t clojure /bin/bash
```

Then within the shell, create a new Leiningen project and start a Clojure REPL.

```
lein new hello-world
cd hello-world
lein repl
```

## Builds

The Dockerfiles are generated by the `docker-clojure` Clojure app in this repo.

You'll need the `tools-deps` distribution of Clojure installed to run the
build. Often this just means installing the `clojure` package for your system. 

The `./build-images.sh` script will generate the Dockerfiles and build all of the images.

## Tests

The `docker-clojure` build tool has a test suite that can be run via the
`./test.sh` script. 
