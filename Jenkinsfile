node {
    stage('Clean workspace') {
        cleanWs()
        echo "done"
    }
    stage('Checkout Repository') {
        checkout scm
        echo "done"
    }
    stage('Build for Python') {
        ls
        dir('CalibrationResults') {
            git url: 'https://github.com/AtlasBID/CalibrationResults.git'
        }
        ls
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