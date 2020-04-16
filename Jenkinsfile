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

    post {
        success {
            dingTalk accessToken:'https://oapi.dingtalk.com/robot/send?access_token=d56b8f2d1d030c4583fc51ef6ad82d5cbe112dd275fa92cd4945c274944949dd',
            imageUrl:'http://webfont.qxsoho.cn/success.png',
            jenkinsUrl:'http://39.96.168.238:8888/',
            message:'小爱同学提示: 人脸识别部署成功！！！',
            notifyPeople:''
        }
        failure {
            dingTalk accessToken:'https://oapi.dingtalk.com/robot/send?access_token=d56b8f2d1d030c4583fc51ef6ad82d5cbe112dd275fa92cd4945c274944949dd',
            imageUrl:'http://webfont.qxsoho.cn/failure.png',
            jenkinsUrl:'http://39.96.168.238:8888/',
            message:'小爱同学提示: 人脸识别部署失败！！！',
            notifyPeople:''
        }
    }
}