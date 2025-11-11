#!/bin/bash

set -e  # Exit immediately if a command fails

terraform init
terraform destroy -auto-approve