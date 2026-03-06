pipeline {
    agent any
    tools { maven 'M3' }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B compile -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn -B package -DskipTests'
            }
        }
        stage('Docker') {
            when { branch 'main' }
            steps {
                script {
                    docker.build("coffee-loyalty:${env.BUILD_NUMBER}")
                }
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
    }
}
