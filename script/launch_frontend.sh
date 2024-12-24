#!/bin/bash

source config.sh

docker run -it --rm -v $HOME:$HOME transema_frontend /bin/bash
