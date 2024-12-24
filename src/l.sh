#!/bin/bash

source ../config.sh

ARGS=
#java -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":"transema/*":"out/transema/*". transema.Main $ARGS
java -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":"transema-with-main":. transema.Main $ARGS