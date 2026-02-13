#!/bin/bash

# Script to add jLOB repository as a submodule to the Deployment repository
# This script is intended to be run from the Deployment repository
#
# Usage:
#   1. Clone the Deployment repository: git clone git@github.com:IAMAB11/Deployment.git
#   2. Navigate to the Deployment repository: cd Deployment
#   3. Download and run this script:
#      curl -sSL https://raw.githubusercontent.com/IAMAB11/jLOB/main/add-to-deployment.sh -o add-to-deployment.sh
#      chmod +x add-to-deployment.sh
#      ./add-to-deployment.sh
#
# Or manually:
#   1. Clone the Deployment repository
#   2. Copy this script to the Deployment repository
#   3. Run: ./add-to-deployment.sh

set -e

JLOB_REPO="https://github.com/IAMAB11/jLOB.git"
SUBMODULE_PATH="data-sources/jLOB"

echo "=== Adding jLOB as a Submodule to Deployment Repository ==="
echo ""

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo "Error: Not in a git repository. Please run this script from the Deployment repository root."
    exit 1
fi

# Check if the submodule already exists
if [ -d "$SUBMODULE_PATH" ]; then
    echo "Warning: Submodule path '$SUBMODULE_PATH' already exists."
    read -p "Do you want to remove and re-add it? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "Removing existing submodule..."
        git submodule deinit -f "$SUBMODULE_PATH"
        git rm -f "$SUBMODULE_PATH"
        rm -rf ".git/modules/$SUBMODULE_PATH"
    else
        echo "Skipping submodule addition."
        exit 0
    fi
fi

# Create the parent directory if it doesn't exist
mkdir -p "$(dirname "$SUBMODULE_PATH")"

# Add the jLOB repository as a submodule
echo "Adding jLOB repository as a submodule at '$SUBMODULE_PATH'..."
git submodule add "$JLOB_REPO" "$SUBMODULE_PATH"

# Initialize and update the submodule
echo "Initializing and updating submodule..."
git submodule update --init --recursive

echo ""
echo "=== Submodule Addition Complete ==="
echo ""
echo "The jLOB repository has been added as a submodule at: $SUBMODULE_PATH"
echo ""
echo "Next steps:"
echo "  1. Commit the changes:"
echo "     git add .gitmodules $SUBMODULE_PATH"
echo "     git commit -m 'Add jLOB as a submodule data source'"
echo ""
echo "  2. Push to the repository:"
echo "     git push origin main"
echo ""
echo "To update the submodule in the future:"
echo "  cd $SUBMODULE_PATH"
echo "  git pull origin main"
echo "  cd ../.."
echo "  git add $SUBMODULE_PATH"
echo "  git commit -m 'Update jLOB submodule'"
echo "  git push origin main"

