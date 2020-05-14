pipeline {
    agent none
    tools{
      maven 'maven3.5'
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
        stage('构建'){
            parallel{
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
                          sh "pwd"
                          sh "printenv"
                          junit allowEmptyResults: true, keepLongStdio: true, testResults: 'target/**/*.xml'
                          sh "mv /root/.jenkins/workspace/pipeline-junit@2/target/sample-0.0.1-SNAPSHOT.jar   /root/.jenkins/workspace/pipeline-junit@2/target/sample.jar"
                        }
                    }
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

    post{
        success {
            dingtalk (
                robot: '993af071-b4d3-4544-a2c2-0464e5cf6e48',
                type: 'LINK',
                title: '小爱同学提示：你有新的消息，请注意查收',
                text: [
                    '人脸识别部署信息',
                    '可爱的小爱同学通知你，项目部署成功',
                    '工作人员可以点击登录Jenkins进行查看'
                ],
                messageUrl: 'http://39.96.168.238:8888/',
                picUrl: 'https://www.picdiet.com/img/photographer_compressed.jpg'
            )
        }

        failure {
            dingtalk (
                robot: '993af071-b4d3-4544-a2c2-0464e5cf6e48',
                type: 'ACTION_CARD',
                title: '小爱同学提示：你有新的消息，请注意查收',
                text: [
                    '![screenshot](@lADOpwk3K80C0M0FoA)',
                    '人脸识别部署信息',
                    '可爱的小爱同学通知你，项目部署失败',
                    '工作人员可以点击登录Jenkins进行查看'
                ],
                messageUrl: 'http://39.96.168.238:8888/',
                picUrl: 'https://www.picdiet.com/img/photographer_compressed.jpg',
                btns: [
                    [
                        title: '内容不错',
                        actionUrl: 'https://www.dingtalk.com/'
                    ],
                    [
                        title: '不感兴趣',
                        actionUrl: 'https://www.dingtalk.com/'
                    ]
                ],
                btnLayout: 'V'
            )
        }
    }
}
