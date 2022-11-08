node {
    stage('Clean workspace') {
        cleanWs()
        echo "done"
    }
    stage('Checkout Service Core') {
        dir('eapp-service-core') {
            checkout(
                [
                    $class: 'GitSCM',
                    branches: [
                        [
                            name: '*/master'
                        ]
                    ],
                    extensions: [],
                    userRemoteConfigs: [
                        [
                            credentialsId: 'multiverse-delivery-github-50gramx',
                            url: 'https://github.com/50gramx/eapp-service-core.git'
                        ]
                    ]
                ]
            )
        }
        echo "done"
    }
    stage('Checkout Python Domain') {
        dir('eapp-python-domain') {
            checkout(
                [
                    $class: 'GitSCM',
                    branches: [
                        [
                            name: '*/master'
                        ]
                    ],
                    extensions: [],
                    userRemoteConfigs: [
                        [
                            credentialsId: 'multiverse-delivery-github-50gramx',
                            url: 'https://github.com/50gramx/eapp-python-domain.git'
                        ]
                    ]
                ]
            )
        }
        echo "done"
    }
    stage('Checkout Nodejs Domain') {
        dir('eapp-nodejs-domain') {
            checkout(
                [
                    $class: 'GitSCM',
                    branches: [
                        [
                            name: '*/master'
                        ]
                    ],
                    extensions: [],
                    userRemoteConfigs: [
                        [
                            credentialsId: 'multiverse-delivery-github-50gramx',
                            url: 'https://github.com/50gramx/eapp-nodejs-domain.git'
                        ]
                    ]
                ]
            )
        }
        echo "done"
    }
    stage('Configure Directories') {

        // Root Directories
        env.EAPP_PROTO_SRC_DIR = "${WORKSPACE}/eapp-service-core/src/main/proto"

        // Dependent Directories
        env.EAPP_PROTO_PYTHON_OUT_DIR = "${WORKSPACE}/eapp-python-domain/eapp-python-domain"
        env.EAPP_PROTO_NODEJS_OUT_DIR = "${WORKSPACE}/eapp-nodejs-domain/eapp-nodejs-domain"


        env.PROTO_INCLUDE_DIRS = """${EAPP_PROTO_SRC_DIR}/google/api/*.proto,
            ${EAPP_PROTO_SRC_DIR}/gramx/fifty/zero/ethos/identity/multiverse/core/entity/epme_1005/*.proto,
            ${EAPP_PROTO_SRC_DIR}/gramx/fifty/zero/ethos/identity/multiverse/core/entity/epme_1005/capability/*.proto,
            ${EAPP_PROTO_SRC_DIR}/gramx/seventy/zero/ethos/gramxpro/multiverse/core/entity/epe_1001/*.proto,
            ${EAPP_PROTO_SRC_DIR}/gramx/seventy/zero/ethos/gramxpro/multiverse/core/entity/epe_1002/*.proto"""
        env.PROTO_INCLUDES = ""
        env.PROTO_INCLUDE_DIRS.tokenize(',\n').each {
            env.TEMP = "${it}"
            env.TEMP = env.TEMP.trim()
            env.PROTO_INCLUDES = "${PROTO_INCLUDES} ${TEMP}"
        }


        sh '''
        # REMOVE EXISTING GENERATED CONTRACTS

        # FOR PYTHON DOMAIN
        rm -rf "$EAPP_PROTO_PYTHON_OUT_DIR/ethos"
        rm -rf "$EAPP_PROTO_PYTHON_OUT_DIR/gramx"

        # FOR NODEJS DOMAIN
        rm -rf "$EAPP_PROTO_NODEJS_OUT_DIR/ethos"
        rm -rf "$EAPP_PROTO_NODEJS_OUT_DIR/gramx"
        '''


        echo "done"
    }
    stage('Build Python Domain') {
        // Depends on
        // grpcio>=1.34.0
        // grpcio-tools>=1.34.0
        // protobuf

        sh '''
        #!/bin/sh

        # GENERATING PROTOFILES
        python3 -m grpc_tools.protoc \
          --python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
          --grpc_python_out=$EAPP_PROTO_PYTHON_OUT_DIR \
          -I $EAPP_PROTO_SRC_DIR \
          --proto_path $PROTO_INCLUDES

        '''

        sh '''
        #!/bin/sh

        # FETCHING THE RELEASE VERSION
        export RELEASE_VERSION=`echo "$releaseVersion" | sed -n -e 2p ${WORKSPACE}/eapp-service-core/release.yaml | sed 's/^.*: //'`
        sed "5s/.*/    version='$RELEASE_VERSION',/" ${WORKSPACE}/eapp-python-domain/setup.py > ${WORKSPACE}/eapp-python-domain/newsetup.py
        mv ${WORKSPACE}/eapp-python-domain/newsetup.py ${WORKSPACE}/eapp-python-domain/setup.py
        '''
        echo "done"
    }
    stage('Push Python Domain') {
        sh '''
        cd eapp-python-domain
        git remote set-url origin https://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@github.com/50gramx/eapp-python-domain.git
        git config --global user.email "amit.khetan.70@50gramx.io"
        git config --global user.name "Amit-Khetan-70"
        git add .
        git commit -m "Added new build"
        git push origin HEAD:master
        '''
        echo "done"
    }
    stage('Build Nodejs Domain') {
        // Depends on node with npm packages
        // grpc-tools

        sh '''
        #!/bin/sh

        # GENERATING PROTOFILES
        npx grpc_tools_node_protoc \
          --js_out=import_style=commonjs,binary:$EAPP_PROTO_NODEJS_OUT_DIR \
          --grpc_out=grpc_js:$EAPP_PROTO_NODEJS_OUT_DIR \
          -I $EAPP_PROTO_SRC_DIR \
          -I /usr/local/include \
          --proto_path $PROTO_INCLUDES

        '''

        sh '''
        #!/bin/sh

        # FETCHING THE RELEASE VERSION
        export RELEASE_VERSION=`echo "$releaseVersion" | sed -n -e 2p ${WORKSPACE}/eapp-service-core/release.yaml | sed 's/^.*: //'`
        sed '3s/.*/  "version": "\${RELEASE_VERSION}",/' ${WORKSPACE}/eapp-nodejs-domain/package.json > ${WORKSPACE}/eapp-nodejs-domain/newpackage.json
        mv ${WORKSPACE}/eapp-nodejs-domain/newpackage.json ${WORKSPACE}/eapp-nodejs-domain/package.json
        '''
        echo "done"
    }
    stage('Push Nodejs Domain') {
        sh '''
        cd eapp-nodejs-domain
        git remote set-url origin https://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@github.com/50gramx/eapp-nodejs-domain.git
        git config --global user.email "amit.khetan.70@50gramx.io"
        git config --global user.name "Amit-Khetan-70"
        git add .
        git commit -m "Added new build"
        git push origin HEAD:master
        '''
        echo "done"
    }
    stage('Configure Multiverse - Delivery - Digital Ocean Node') {
        withKubeConfig(
            clusterName: 'microk8s-cluster',
            contextName: 'microk8s',
            credentialsId: 'multiverse-india-do-config',
            namespace: 'multiverse-delivery',
            serverUrl: 'https://157.245.106.167:16443') {
            sh 'kubectl apply -f ${WORKSPACE}/eapp-service-core/playbook/multiverse-delivery-namespace.yaml'
            sh 'kubectl apply -f ${WORKSPACE}/eapp-service-core/playbook/multiverse-delivery-ingress.yaml'
        }
        echo "done"
    }
    stage('Clean workspace') {
        cleanWs()
        echo "done"
    }
}