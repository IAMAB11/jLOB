#!/bin/bash

# Deployment script for jLOB application
# This script sets up deployment remote and pushes artifacts to deployment repository
#
# Note: Uses 'deployment' as remote name to avoid conflicts with existing 'origin' remote

set -e

DEPLOYMENT_REMOTE="git@github.com:IAMAB11/Deployment.git"
REMOTE_NAME="deployment"

echo "=== jLOB Deployment Script ==="

# Check if deployment remote already exists
if git remote get-url "$REMOTE_NAME" >/dev/null 2>&1; then
    echo "Remote '$REMOTE_NAME' already exists:"
    git remote -v | grep "$REMOTE_NAME"
    read -p "Do you want to update it? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        git remote remove "$REMOTE_NAME"
        git remote add "$REMOTE_NAME" "$DEPLOYMENT_REMOTE"
        echo "Remote '$REMOTE_NAME' updated."
    fi
else
    git remote add "$REMOTE_NAME" "$DEPLOYMENT_REMOTE"
    echo "Remote '$REMOTE_NAME' added successfully."
fi

echo ""
echo "Current remotes:"
git remote -v

echo ""
echo "=== Deployment remote setup complete ==="
echo ""
echo "To build and prepare for deployment, run:"
echo "  gradle bookJar -x test -x generateJLobJooqSchemaSource"
echo ""
echo "To push to deployment repository:"
echo "  git push $REMOTE_NAME <branch-name>"
