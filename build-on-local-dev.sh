#!/bin/bash

WORKSPACE=/opt/ethos/apps/service
# Root Directories
EAPP_PROTO_SRC_DIR="${WORKSPACE}/eapp-service-core/src/main/proto"

# Dependent Directories
EAPP_PROTO_PYTHON_OUT_DIR="${WORKSPACE}/eapp-python-domain/src/eapp_python_domain"

declare -a proto_include_folders=(
                "${EAPP_PROTO_SRC_DIR}/google/api/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/entities/*.proto"
            )

echo ${proto_include_folders[@]}
#"${proto_include_folders[@]}"

# GENERATING PROTOFILES
python3 -m grpc_tools.protoc \
  --python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
  --grpc_python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
  -I $EAPP_PROTO_SRC_DIR \
  --proto_path ${proto_include_folders[@]}