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
        sh '''
        #!/bin/sh
        ls
        '''
        dir('eapp-python-domain') {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'multiverse-delivery-github-50gramx', url: 'https://github.com/50gramx/eapp-python-domain.git']]])
        }
        sh '''
        #!/bin/sh
        ls eapp-python-domain
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