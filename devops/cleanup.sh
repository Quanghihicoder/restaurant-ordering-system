#!/bin/bash

set -e  # Exit immediately if a command fails

# Safety destroy
./destroy.sh

# Delete terraform state bucket
aws s3 rb s3://qfood-terraform --force --region ap-southeast-2

# Delete terraform lock table
aws dynamodb delete-table \
  --table-name qfood-terraform-lock \
  --region ap-southeast-2 \
  --no-cli-pager