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



    post{
         success {
            dingtalk (
                robot: '993af071-b4d3-4544-a2c2-0464e5cf6e48',
                type: 'LINK',
                title: '小爱同学提示：你有新的消息，请注意查收',
                text: [
                    '人脸识别部署信息',
                    '可爱的小爱同学通知你，项目部署成功'
                ],
                messageUrl: 'http://www.baidu.com',
                picUrl: 'https://www.picdiet.com/img/photographer_compressed.jpg'
            )
        }
    }
}