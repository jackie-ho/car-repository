#!/bin/bash
# Script to transform teacher directories to student directories

echo "Deleteing solution code"
find . -name 'solution-code' -exec rm -rf {} \;

echo "Deleting lab folders"
find . -name '*-lab' -exec rm -rf {} \;

echo "Deleting instructor comments and checks"
find . -type f -name 'readme.md' -print0 | xargs -0 sed -i '' '/^>/ d'
