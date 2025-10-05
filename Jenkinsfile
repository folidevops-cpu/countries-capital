pipeline {
    agent any
    params {
        choice(name: 'VERSION', choices: ['1.0.0', '1.0.1', '1.0.2'], description: 'Select the branch to build')
        booleanParam(name: 'executeTests', defaultValue: true, description: 'Execute tests')
    }
    stages {
        stage('Build') {
            steps {
               echo 'Building the application...'
            }
        }
        stage('Test') {
            when {
                expression { return params.executeTests }
            }
            steps {
               echo 'Running tests...'
            }
        }
        stage('Deploy') {
            steps {
               echo 'Deploying the application...'
            }
        }
    }
}