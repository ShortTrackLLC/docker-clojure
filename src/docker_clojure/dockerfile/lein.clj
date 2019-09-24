(ns docker-clojure.dockerfile.lein)

(defn install-deps [{:keys [distro]}]
  (case distro
    "slim-buster"
    ["RUN apt-get update && apt-get install -y wget gnupg"]

    "alpine"
    ["RUN apk add --update --no-cache tar gnupg bash openssl"]

    nil))

(defn contents [{:keys [build-tool-version] :as variant}]
  (-> [(format "ENV LEIN_VERSION=%s" build-tool-version)
       "ENV LEIN_INSTALL=/usr/local/bin/"
       ""
       "WORKDIR /tmp"
       ""]
      (concat (install-deps variant))
      (concat
       [""
        "# Download the whole repo as an archive"
        "RUN mkdir -p $LEIN_INSTALL \\"
        "  && wget -q https://raw.githubusercontent.com/technomancy/leiningen/$LEIN_VERSION/bin/lein-pkg \\"
        "  && echo \"Comparing lein-pkg checksum ...\" \\"
        "  && sha1sum lein-pkg \\"
        "  && echo \"93be2c23ab4ff2fc4fcf531d7510ca4069b8d24a *lein-pkg\" | sha1sum -c - \\"
        "  && mv lein-pkg $LEIN_INSTALL/lein \\"
        "  && chmod 0755 $LEIN_INSTALL/lein \\"
        "  && wget -q https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip \\"
        "  && wget -q https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip.asc \\"
        "  && gpg --batch --keyserver pool.sks-keyservers.net --recv-key 2B72BF956E23DE5E830D50F6002AF007D1A7CC18 \\"
        "  && echo \"Verifying Jar file signature ...\" \\"
        "  && gpg --verify leiningen-$LEIN_VERSION-standalone.zip.asc \\"
        "  && rm leiningen-$LEIN_VERSION-standalone.zip.asc \\"
        "  && mkdir -p /usr/share/java \\"
        "  && mv leiningen-$LEIN_VERSION-standalone.zip /usr/share/java/leiningen-$LEIN_VERSION-standalone.jar"
        ""
        "ENV PATH=$PATH:$LEIN_INSTALL"
        "ENV LEIN_ROOT 1"
        ""
        "# Install clojure 1.10.0 so users don't have to download it every time"
        "RUN echo '(defproject dummy \"\" :dependencies [[org.clojure/clojure \"1.10.1\"]])' > project.clj \\"
        "  && lein deps && rm project.clj"
        ""
        "CMD [\"lein\", \"repl\"]"])
      (->> (remove nil?))))
