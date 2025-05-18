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
                    bat 'mvn clean package -DskipTests'
                }
            }

            stage('Test') {
                steps {
                    bat 'mvn test'
                }
            }

            stage('Allure Report Generation') {
                steps {
                    bat 'mvn allure:report'
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
            always {
                allure([
                            includeProperties: false,
                            jdk: '',
                            properties: [],
                            reportBuildPolicy: 'ALWAYS',
                            results: [[path: 'target/allure-results']]
                        ])
            }
        }
}
