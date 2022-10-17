node {
    stage('Clean workspace') {
        cleanWs()
        echo "done"
    }
    stage('Checkout Repository') {
        dir('eapp-service-core') {
            credentialsId: 'multiverse-delivery-github-50gramx'
            git url: 'https://github.com/50gramx/eapp-service-core.git'
        }
        echo "done"
    }
    stage('Build for Python') {
        sh '''
        #!/bin/sh
        ls
        '''
        dir('CalibrationResults') {
            git url: 'https://github.com/AtlasBID/CalibrationResults.git'
        }
        sh '''
        #!/bin/sh
        ls
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