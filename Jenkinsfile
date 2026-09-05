
pipeline {
    agent any
    tools {
        maven 'M3'
    }
    stages {
        stage('Code Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Kiran37614/new-repo.git'
            }
        }
        stage('Maven Test & Compile') {
            steps {
                // The -f flag forces Maven to read the pom.xml inside the subfolder
                sh 'mvn -f restaurant-table-reservation-system/pom.xml clean package -DskipTests=true'
            }
        }
        stage('SonarQube Static Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    // Force the Sonar scanner to read from the subfolder pom.xml
                    sh 'mvn -f restaurant-table-reservation-system/pom.xml sonar:sonar'
                }
            }
        }
        stage('Docker Image Construction & Deployment') {
            steps {
                // Switch contexts into the subfolder to pick up the jar target binaries
                dir('restaurant-table-reservation-system') {
                    sh '''
                    docker build -t restaurant-app:latest .
                    docker rm -f restaurant-container || true
                    docker run -d --name restaurant-container -p 8080:8080 restaurant-app:latest
                    '''
                }
            }
        }
    }
}
