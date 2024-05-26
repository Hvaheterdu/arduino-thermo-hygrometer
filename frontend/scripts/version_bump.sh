#!/bin/bash

# Check if a version bump type argument is provided
if [ -z "$1" ]; then
  echo "Usage: $0 {patch|minor|major}"
  exit 1
fi

BUMP_TYPE=$1

# Check if the provided argument is valid
if [[ "$BUMP_TYPE" != "patch" && "$BUMP_TYPE" != "minor" && "$BUMP_TYPE" != "major" ]]; then
  echo "Invalid version argument. Use \"patch\", \"minor\", or \"major\"."
  exit 1
fi

# Get the current version from package.json
CURRENT_VERSION=$(jq -r '.version' ./package.json)

# Bump the version
npm version $BUMP_TYPE --no-git-tag-version

# Get the new version from package.json
NEW_VERSION=$(jq -r '.version' ./package.json)

# Stage the package.json and package-lock.json files
git add .

# Commit the changes
git commit -m "Bump version from $CURRENT_VERSION to $NEW_VERSION"

echo "Version bumped from $CURRENT_VERSION to $NEW_VERSION and changes committed successfully."
