def gv

pipeline {
    agent any
    parameters {
        choice(name: 'VERSION', choices: ['1.0.0', '1.0.1', '1.0.2'], description: 'Select the branch to build')
        booleanParam(name: 'executeTests', defaultValue: true, description: 'Execute tests')
    }
    stages {

        stage('init'){
            steps {
                script {
                    gv = load 'script.groovy'
                }
            }
        }


        stage('Build') {
            steps {
              script {
                  gv.buildApp()
              }
            }
        }
        stage('Test') {
            when {
                expression { return params.executeTests }
            }
            steps {
               script {
                   gv.testApp()
               }
            }
        }
        stage('Deploy') {
            steps {
               script {
                   gv.deployApp()
               }
            }
        }
    }
}