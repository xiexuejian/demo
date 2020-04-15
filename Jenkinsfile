pipeline {
    agent { node {label 'master'}}
    tools{
      maven 'maven3.5'
    }
    stages {
       stage('拉取代码') {

            steps {
               checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '3179d59449a89e5a27c52003173267b7902bbfc2', url: 'https://github.com/xiexuejian/demo.git']]])
            }
        }

        stage('开始构建') {
            steps {
                dir(env.WORKSPACE){
                  sh "mvn clean  install"
                  sh "printenv"
                  junit allowEmptyResults: true, keepLongStdio: true, testResults: 'target/**/*.xml'
                  sh "mv target/sample-0.0.1-SNAPSHOT.jar target/sample.jar"
                }
            }
        }
        stage('Sonarqube') {

          steps {
              dir(env.WORKSPACE){
                sh "mvn sonar:sonar -Dsonar.host.url=http://121.36.31.229:9000 -Dsonar.login=f4a34690771d6da24bd3fa9a94f33eefa6cf05d8"
              }
          }
        }

        stage('开始运行'){
          steps{
            withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
          	retry(3) {
          		script{
          			sh 'nohup java -jar ./target/sample.jar  &'
          		}
          	}
          }
          }
        }
    }
}