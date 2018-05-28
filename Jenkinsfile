node {
   def mvnHome
   stage('checkout') {
      git 'git@github.com:zach007/Chat.git'
      echo 'check out from git@github.com:zach007/Chat.git'
      mvnHome = tool 'maven_3.5'
   }
   stage('building') {
     echo 'build project'
     sh 'mvn clean install '
   }

   stage('testing'){
        echo 'running Junit testing'
        sh 'mvn clean surefire:test'
   }

  stage('SonarQube analysis') {
    withSonarQubeEnv('docker_sonar') {
       sh 'mvn clean verify sonar:sonar -o'
    }
  }

    stage('package') {
        echo 'package with maven'
        sh 'mvn clean package'
    }

   stage('deploy to nexus') {
        echo 'deploy to nexus with maven'
        sh 'mvn clean deploy'
   }

   stage('pull code to SAT[docker]') {
        echo '集成测试'
   }

   stage('UAT') {
        echo '验收测试'
   }

   stage('PROD') {
        echo  'release to Production with param'
   }
}