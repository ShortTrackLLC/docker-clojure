FROM java:8
MAINTAINER Paul Lam <paul@quantisan.com>

ENV LEIN_VERSION=2.5.3
ENV LEIN_INSTALL=/usr/local/bin/

WORKDIR /tmp

# Download the whole repo as an archive
RUN mkdir -p $LEIN_INSTALL \
  && wget --quiet https://github.com/technomancy/leiningen/archive/$LEIN_VERSION.tar.gz \
  && echo "Comparing archive checksum ..." \
  && echo "871d2e308076d2e9edf457cffc9d15996c8d003e *$LEIN_VERSION.tar.gz" | sha1sum -c - \

  && mkdir ./leiningen \
  && tar -xzf $LEIN_VERSION.tar.gz  -C ./leiningen/ --strip-components=1 \
  && mv leiningen/bin/lein-pkg $LEIN_INSTALL/lein \
  && rm -rf $LEIN_VERSION.tar.gz ./leiningen \

  && chmod 0755 $LEIN_INSTALL/lein \

# Download and verify Lein stand-alone jar
  && wget --quiet https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip \
  && wget --quiet https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip.asc \

  && gpg --keyserver pool.sks-keyservers.net --recv-key 2E708FB2FCECA07FF8184E275A92E04305696D78 \
  && echo "Verifying Jar file signature ..." \
  && gpg --verify leiningen-$LEIN_VERSION-standalone.zip.asc \

# Put the jar where lein script expects
  && rm leiningen-$LEIN_VERSION-standalone.zip.asc \
  && mv leiningen-$LEIN_VERSION-standalone.zip /usr/share/java/leiningen-$LEIN_VERSION-standalone.jar

# Some REPLs (e.g., Figwheel) necessitate a readline wrapper.
RUN apt-get update && apt-get install rlfe && rm -rf /var/lib/apt/lists/*

ENV PATH=$PATH:$LEIN_INSTALL
ENV LEIN_ROOT 1

RUN lein
