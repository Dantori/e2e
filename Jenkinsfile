pipeline {
    agent any

    tools {
        jdk 'jdk_21.0.6_7'
        maven 'Maven_3.9.9'
    }

    stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Build') {
                steps {
                    bat 'mvn clean package'
                }
            }

            stage('Test') {
                steps {
                    bat 'mvn test'
                }
            }

            stage('Archive Artifacts') {
                steps {
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                }
            }
        }

        post {
            success {
                echo '✅ Сборка завершена успешно!'
            }
            failure {
                echo '❌ Сборка завершилась с ошибками!'
            }
        }
}
