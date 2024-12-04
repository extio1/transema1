FROM dokken/ubuntu-18.04

LABEL name="\q"

RUN apt-get update
RUN apt-get install -y build-essential m4 openjdk-8-jdk libgmp-dev libmpfr-dev
RUN apt-get install -y pkg-config flex z3 libz3-dev maven opam python3 cmake gcc
RUN apt-get install -y clang-8 lld-8 llvm-8-tools zlib1g-dev libboost-test-dev libyaml-dev libjemalloc-dev
RUN apt-get install -y git nano wget

RUN opam init

RUN wget https://github.com/runtimeverification/k/releases/download/v5.0.0-40214a3ce/kframework_5.0.0_amd64_bionic.deb
RUN apt-get install ./kframework_5.0.0_amd64_bionic.deb

RUN apt-get install time libfile-chdir-perl
