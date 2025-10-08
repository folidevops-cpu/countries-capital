pipeline {
    agent any
    tools {
        maven 'maven-3.9.11'
    
    }
    stages {

        stage('build jar') {
            steps {
              script {
                    echo "Building jar file..."
                    sh 'mvn  package'
                    
              }
            }
        }
        stage('build docker image') {
            
            steps {
               script {
                   echo "Building docker image..."
                   withCredentials([usernamePassword(credentialsId: 'dockerhub-password', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                       sh 'docker build -t folidevops/java-app:latest .'
                       sh ' echo $DOCKERHUB_PASSWORD | docker login -u "$DOCKERHUB_USERNAME" --password-stdin'
                       sh 'docker push folidevops/java-app:latest'
                   }
               }
            }
        }
        stage('Deploy') {
            steps {
               script {
                     echo "Deploying the application..."
               }
            }
        }
    }
}
