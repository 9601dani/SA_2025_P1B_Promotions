pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Haciendo checkout del repositorio..."
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                dir('promotion'){
                    sh './mvnw clean install'
                }
            }
        }

        stage('Verify Jacoco Exec') {
            steps {
                dir('promotion/target') {
                    sh 'ls -l'
                }
            }
        }
    }

    post {
        success {
            script {
                jacoco (
                    execPattern: '**/target/*.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src/main/java',
                    changeBuildStatus: true,
                    minimumLineCoverage: '50'
                )
            }
            echo 'Build y cobertura completados con éxito'
        }

        failure {
            echo 'Build o tests fallaron'
        }
    }
}
