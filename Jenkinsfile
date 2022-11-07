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
    stage('Configure Directories') {
        env.EAPP_PROTO_SRC_DIR = "${WORKSPACE}/eapp-service-core/src/main/proto"
        env.EAPP_PROTO_PYTHON_OUT_DIR = "${WORKSPACE}/eapp-python-domain/eapp-python-domain"


        env.PROTO_INCLUDE_DIRS = """${EAPP_PROTO_SRC_DIR}/google/api/*.proto,
            ${EAPP_PROTO_SRC_DIR}/gramx/fifty/zero/ethos/identity/multiverse/core/entity/epme_1001/*.proto,
            ${EAPP_PROTO_SRC_DIR}/gramx/fifty/zero/ethos/identity/multiverse/core/entity/epme_1001/capability/context/discover/*.proto,
            ${EAPP_PROTO_SRC_DIR}/gramx/fifty/zero/ethos/identity/multiverse/core/entity/epme_1005/*.proto"""
        env.PROTO_INCLUDES = ""
        env.PROTO_INCLUDE_DIRS.tokenize(',\n').each {
            env.TEMP = "${it}"
            env.TEMP = env.TEMP.trim()
            env.PROTO_INCLUDES = "${PROTO_INCLUDES} ${TEMP}"
        }


        sh '''
        # REMOVE EXISTING GENERATED PROTOFILES
        rm -rf "$EAPP_PROTO_PYTHON_OUT_DIR/ethos"
        rm -rf "$EAPP_PROTO_PYTHON_OUT_DIR/gramx"
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
    stage('Delivery - Multiverse - Digital Ocean Node - Host PyPi Packaging') {
        withKubeConfig(
            clusterName: 'microk8s-cluster',
            contextName: 'microk8s',
            credentialsId: 'multiverse-india-do-config',
            namespace: 'multiverse-delivery',
            serverUrl: 'https://157.245.106.167:16443') {
            sh 'kubectl apply -f playbook/multiverse-delivery-namespace.yaml'
            sh 'kubectl apply -f playbook/multiverse-delivery-ingress.yaml'
        }
        echo "done"
    }
    stage('Clean workspace') {
        cleanWs()
        echo "done"
    }
}