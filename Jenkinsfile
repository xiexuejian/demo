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

    post {
        always{
            emailext body:
                '''<body leftmargin="8" marginwidth="0" topmargin="8" marginheight="4"
                       offset="0">
                       <table width="95%" cellpadding="0" cellspacing="0"  style="font-size: 11pt; font-family: Tahoma, Arial, Helvetica, sans-serif">
                           <tr>
                               本邮件由系统自动发出，无需回复！<br/>
                               各位同事，大家好，以下为${PROJECT_NAME }项目构建信息</br>
                               <td><font color="#CC0000">构建结果 - ${BUILD_STATUS}</font></td>
                           </tr>
                           <tr>
                               <td><br />
                               <b><font color="#0B610B">构建信息</font></b>
                               <hr size="2" width="100%" align="center" /></td>
                           </tr>
                           <tr>
                               <td>
                                   <ul>
                                       <li>项目名称 ： ${PROJECT_NAME}</li>
                                       <li>构建编号 ： 第${BUILD_NUMBER}次构建</li>
                                       <li>触发原因： ${CAUSE}</li>
                                       <li>构建状态： ${BUILD_STATUS}</li>
                                       <li>构建日志： <a href="${BUILD_URL}console">${BUILD_URL}console</a></li>
                                       <li>构建  Url ： <a href="${BUILD_URL}">${BUILD_URL}</a></li>
                                       <li>工作目录 ： <a href="${PROJECT_URL}ws">${PROJECT_URL}ws</a></li>
                                       <li>项目  Url ： <a href="${PROJECT_URL}">${PROJECT_URL}</a></li>
                                   </ul>

                   <h4><font color="#0B610B">失败用例</font></h4>
                   <hr size="2" width="100%" />
                   $FAILED_TESTS<br/>

                   <h4><font color="#0B610B">最近提交(#$SVN_REVISION)</font></h4>
                   <hr size="2" width="100%" />
                   <ul>
                   ${CHANGES_SINCE_LAST_SUCCESS, reverse=true, format="%c", changesFormat="<li>%d [%a] %m</li>"}
                   </ul>
                   详细提交: <a href="${PROJECT_URL}changes">${PROJECT_URL}changes</a><br/>

                               </td>
                           </tr>
                       </table>
                   </body>  ''',
            subject: '[测试邮件通知] ${PROJECT_NAME} - Build # ${BUILD_NUMBER} - ${BUILD_STATUS}!',
            to: '1521107575@qq.com',
            from: 'xxjxiexuejian@163.com'
        }
    }
}