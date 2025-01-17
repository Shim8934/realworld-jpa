#!/usr/bin/env bash
set -x

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

export USERNAME="shimkibanggi"
export PASSWORD="testPassword"

APIURL=${APIURL:-http://localhost:8080/api}
# USERNAME=${USERNAME:-u`date +%s`}
EMAIL=${EMAIL:-$USERNAME@example.com}
# PASSWORD=${PASSWORD:-password}

npx newman run $SCRIPTDIR/Conduit.postman_collection.json \
  --global-var "APIURL=$APIURL" \
  --global-var "USERNAME=$USERNAME" \
  --global-var "EMAIL=$EMAIL" \
  --global-var "PASSWORD=$PASSWORD" \
  "$@"