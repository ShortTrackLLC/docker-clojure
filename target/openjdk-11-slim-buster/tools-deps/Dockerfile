FROM openjdk:11-slim-buster

ENV CLOJURE_VERSION=1.10.1.483

WORKDIR /tmp

RUN \
apt-get update && \
apt-get install -y curl rlwrap wget && \
rm -rf /var/lib/apt/lists/* && \
wget https://download.clojure.org/install/linux-install-$CLOJURE_VERSION.sh && \
chmod +x linux-install-$CLOJURE_VERSION.sh && \
./linux-install-$CLOJURE_VERSION.sh && \
clojure -e "(clojure-version)" && \
apt-get remove -y --purge curl wget

# Docker bug makes rlwrap crash w/o short sleep first
# Bug: https://github.com/moby/moby/issues/28009
# As of 2019-10-2 this bug still exists, despite that issue being closed
CMD ["sh", "-c", "sleep 1 && exec clj"]