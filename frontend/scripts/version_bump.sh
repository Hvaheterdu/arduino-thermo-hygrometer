#!/bin/bash

# Change to the project root directory
cd "$(dirname "$0")"
while [ ! -f package.json ]; do
    if [ "$(pwd)" = "/" ]; then
        echo "Error: package.json not found in the directory hierarchy."
        exit 1
    fi
    cd ..
done

# Check if a version bump type argument is provided
if [ -z "$1" ]; then
  echo "Usage: $0 {patch|minor|major}"
  exit 1
fi

BUMP_TYPE=$1

echo "Automatically performing a $BUMP_TYPE update to project."
echo

# Check if the provided argument is valid
if [[ "$BUMP_TYPE" != "patch" && "$BUMP_TYPE" != "minor" && "$BUMP_TYPE" != "major" ]]; then
  echo "Invalid version type. Use \"patch\", \"minor\", or \"major\"."
  exit 1
fi

# Get the current version from package.json
CURRENT_VERSION=$(jq -r '.version' package.json)

echo "Current project version: $CURRENT_VERSION"
echo

# Bump the version
echo "New project version: $(npm version $BUMP_TYPE --no-git-tag-version)"
echo

# Get the new version from package.json
NEW_VERSION=$(jq -r '.version' package.json)

# Stage the package.json and package-lock.json files
git add .

# Commit the changes
git commit -m "Bump version from $CURRENT_VERSION to $NEW_VERSION"

echo
echo "Version bumped from $CURRENT_VERSION to $NEW_VERSION and changes committed successfully."
