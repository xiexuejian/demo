pipeline {
    agent none
    tools{
      maven 'maven3.5'
      ansible 'MyAnsible'
    }
    environment{
       registry = "121.36.31.229:8595"
       pingZheng = 'ddb40bf6-06ae-4728-881f-a8459909209a'
     }
    stages {
       stage('拉取代码') {
            agent {node {label 'master'}}
            steps {
               checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '3179d59449a89e5a27c52003173267b7902bbfc2', url: 'https://github.com/xiexuejian/demo.git']]])
            }
        }
        stage('代码质量检测') {
            agent {node {label 'master'}}
           steps {
               dir(env.WORKSPACE){
                 sh "mvn sonar:sonar -Dsonar.host.url=http://121.36.31.229:9000 -Dsonar.login=f4a34690771d6da24bd3fa9a94f33eefa6cf05d8"
               }
           }
         }

        stage('开始构建') {
        agent {node {label 'master'}}
            steps {
                dir(env.WORKSPACE){
                  sh "mvn clean  install"
                  sh "printenv"
                  junit allowEmptyResults: true, keepLongStdio: true, testResults: 'target/**/*.xml'
                  sh "mv target/sample-0.0.1-SNAPSHOT.jar target/sample.jar"
                }
            }
        }

        stage('执行ansible'){
            agent {node {label 'master'}}
            steps{
                ansiblePlaybook disableHostKeyChecking: true, installation: 'MyAnsible', inventory: 'ansible/hosts', playbook: 'ansible/deploy.yml'
            }
        }
        stage('初始化docker环境') {
        agent {node {label 'master'}}
           steps {
                script{
                  def dockerPath = tool 'docker19'
                  env.PATH = "${dockerPath}/bin:${env.PATH}"
                }
           }
        }
        stage('构建镜像并发布到Nexus') {
           agent {node {label 'master'}}
            steps{
                withDockerRegistry([
                   credentialsId:"${pingZheng}",
                   url:"http://${registry}"
                ]){
                   sh "docker build -t ${registry}/jenkins:v3  ."
                   sh "docker push ${registry}/jenkins:v3"
                }
            }
        }
    }
}
