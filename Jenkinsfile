pipeline {
    agent { node {label 'master'}}
    tools{
      maven 'mymaven'
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

        stage('开始运行'){
          steps{
            script{
              sh " nohup java -jar ./target/sample.jar  &"
              sh "sleep 60"
            }
          }
        }
    }
}