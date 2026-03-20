#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROPERTIES_FILE="${SCRIPT_DIR}/gradle/wrapper/gradle-wrapper.properties"

if [ ! -f "${PROPERTIES_FILE}" ]; then
  echo "gradle-wrapper.properties not found" >&2
  exit 1
fi

DIST_URL="$(grep distributionUrl "${PROPERTIES_FILE}" | cut -d'=' -f2-)"
DIST_NAME="${DIST_URL##*/}"
DIST_VERSION="${DIST_NAME%-bin.zip}"
GRADLE_DIR="${HOME}/.gradle/wrapper/dists/${DIST_VERSION}-bin"
GRADLE_BIN="${GRADLE_DIR}/gradle-${DIST_VERSION}/bin/gradle"

if [ ! -x "${GRADLE_BIN}" ]; then
  echo "Downloading Gradle ${DIST_VERSION}..."
  mkdir -p "${GRADLE_DIR}"
  TMP_ZIP="$(mktemp)"
  curl -L "${DIST_URL}" -o "${TMP_ZIP}"
  unzip -q "${TMP_ZIP}" -d "${GRADLE_DIR}"
  rm -f "${TMP_ZIP}"
fi

exec "${GRADLE_BIN}" "$@"
