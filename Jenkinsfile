pipeline {
    agent { node {label 'master'}}
    tools{
      maven 'maven3.5'
    }
    stages {
       stage('拉取代码') {

            steps {
               checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '3179d59449a89e5a27c52003173267b7902bbfc2', url: 'https://github.com/xiexuejian/demo.git']]])
               env.check_to_tag="1.0"
            }
        }
        stage('代码质量检测') {
           steps {
               dir(env.WORKSPACE){
                 sh "mvn sonar:sonar -Dsonar.host.url=http://121.36.31.229:9000 -Dsonar.login=f4a34690771d6da24bd3fa9a94f33eefa6cf05d8"
               }
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
        stage('初始化docker环境') {
           steps {
                script{
                  def dockerPath = tool 'docker19'
                  env.PATH = "${dockerPath}/bin:${env.PATH}"
                }
           }
        }
        stage('构建镜像并发布到harbor') {
            steps{
                script{
                    docker.withRegistry('http://39.96.168.238', 'c004b825-af53-4364-b247-79edff726aa1'){
                        def BuildImage = docker.build("http://39.96.168.238/xxj/http:${check_to_tag}")
                        BuildImage.push()
                    }
                }
            }
        }
        stage('运行docker命令') {
            steps {
                script{
                  sh "docker --version"
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
