#!/bin/bash

WORKSPACE=/opt/ethos/apps/service
# Root Directories
EAPP_PROTO_SRC_DIR="${WORKSPACE}/eapp-service-core/src/main/proto"

# Dependent Directories
EAPP_PROTO_PYTHON_OUT_DIR="${WORKSPACE}/eapp-python-domain/src/eapp_python_domain"

rm -rf $EAPP_PROTO_PYTHON_OUT_DIR/*

declare -a proto_include_folders=(
                "${EAPP_PROTO_SRC_DIR}/google/api/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/entities/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/account_assistant/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/galaxy/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/machine/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/organisation/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/identity/space/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/action/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/conversation/message/account_assistant/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/product/knowledge/space_knowledge_domain_file_page_para/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/cognitive/assist/context/*.proto"
                "${EAPP_PROTO_SRC_DIR}/ethos/elint/services/cognitive/assist/knowledge/*.proto"
            )

echo ${proto_include_folders[@]}
#"${proto_include_folders[@]}"

# GENERATING PROTOFILES
python3 -m grpc_tools.protoc \
  --python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
  --grpc_python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
  -I $EAPP_PROTO_SRC_DIR \
  --proto_path ${proto_include_folders[@]}

find ${EAPP_PROTO_PYTHON_OUT_DIR}/ethos/ -type d -exec touch {}/__init__.py \;