node {
    stage('Clean workspace') {
        cleanWs()
        echo "done"
    }
    stage('Checkout Repository') {
        dir('eapp-service-core') {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'multiverse-delivery-github-50gramx', url: 'https://github.com/50gramx/eapp-service-core.git']]])
        }
        echo "done"
    }
    stage('Build for Python') {
        // Depends on
        // grpcio>=1.34.0
        // grpcio-tools>=1.34.0

        dir('eapp-python-domain') {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'multiverse-delivery-github-50gramx', url: 'https://github.com/50gramx/eapp-python-domain.git']]])
        }


        sh '''
        #!/bin/sh

        echo "$(date) :: Building Python Services"

        # PROTO GENERATION DIR CONFIG
        EAPP_PROTO_SRC_DIR=`pwd`/eapp-service-core/src/main/proto
        EAPP_PROTO_PYTHON_OUT_DIR=`pwd`/eapp-python-domain/src



        # PROTO SOURCE DIRS
        declare -a proto_include_folders=(
          $EAPP_PROTO_SRC_DIR/google/api/*.proto
          $EAPP_PROTO_SRC_DIR/ethos/elint/entities/*.proto
          $EAPP_PROTO_SRC_DIR/gramx/fifty/zero/ethos/identity/multiverse/epme/*.proto
        )

        # REMOVE EXISTING GENERATED PROTOFILES
        rm -rf "$EAPP_PROTO_PYTHON_OUT_DIR/ethos"
        rm -rf "$EAPP_PROTO_PYTHON_OUT_DIR/gramx"

        # GENERATING PROTOFILES
        python3 -m grpc_tools.protoc \
          --python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
          --grpc_python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
          -I $EAPP_PROTO_SRC_DIR \
          --proto_path "${proto_include_folders[@]}"

        ls $EAPP_PROTO_PYTHON_OUT_DIR/ethos
        ls $EAPP_PROTO_PYTHON_OUT_DIR/ethos/elint/entities
        '''


        sh '''
        #!/bin/sh
        ls eapp-python-domain
        '''

        echo "Let's try pushing the code"
        sh '''
        cd eapp-python-domain
        git remote set-url origin https://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@github.com/50gramx/eapp-python-domain.git
        git config --global user.email "amit.khetan.70@50gramx.io"
        git config --global user.name "Amit-Khetan-70"
        git add .
        git commit -m "Added new build"
        git push origin HEAD:master
        '''
//         get the version to tag
// generate the proto compiled code
// update the eapp-python-domain setup.py
// push the generated code to eapp-python-domain
        echo "done"
    }
    stage('Build for Node.js') {
        echo "done"
    }
    stage('Clean workspace') {
        cleanWs()
        echo "done"
    }
}