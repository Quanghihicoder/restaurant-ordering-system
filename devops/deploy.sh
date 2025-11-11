#!/bin/bash

set -e  # Exit immediately if a command fails

terraform init
terraform apply -auto-approve