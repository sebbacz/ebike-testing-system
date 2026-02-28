#!/usr/bin/env bash
# destroy_infra.sh – tear down an Azure resource group
# Requires these env vars:
#   AZURE_CLIENT_ID
#   AZURE_CLIENT_SECRET
#   AZURE_TENANT_ID
#   AZURE_SUBSCRIPTION_ID
#   AZURE_RESOURCE_GROUP

set -euo pipefail
IFS=$'\n\t'

check_vars() {
  local missing=0
  for v in AZURE_CLIENT_ID AZURE_CLIENT_SECRET AZURE_TENANT_ID AZURE_SUBSCRIPTION_ID AZURE_RESOURCE_GROUP; do
    if [[ -z "${!v:-}" ]]; then
      echo "ERROR: $v is not set" >&2
      missing=1
    fi
  done
  (( missing == 0 )) || exit 1
}

main() {
  check_vars

  echo "Logging in to Azure as service principal..."
  az login --service-principal \
    -u "$AZURE_CLIENT_ID" \
    -p "$AZURE_CLIENT_SECRET" \
    --tenant "$AZURE_TENANT_ID" >/dev/null

  echo "Selecting subscription $AZURE_SUBSCRIPTION_ID..."
  az account set --subscription "$AZURE_SUBSCRIPTION_ID"

  echo "Deleting resource group '$AZURE_RESOURCE_GROUP'..."
  az group delete --name "$AZURE_RESOURCE_GROUP" --yes --no-wait

  echo "Destroy initiated."
}

main "$@"
