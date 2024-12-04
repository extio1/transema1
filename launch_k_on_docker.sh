#!/bin/bash

# TODO throw away hardcode /media/extio1:$HOME
docker run -it --rm -v /media/extio1:$HOME kframework_release_5_deb /bin/bash
