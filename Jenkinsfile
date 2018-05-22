node {
   def mvnHome
   stage('checkout') {
      git 'git@github.com:zach007/Chat.git'
      echo 'check out from git@github.com:zach007/Chat.git'
      mvnHome = tool 'maven_3.5'
   }

  stage('SonarQube analysis') {
    withSonarQubeEnv('docker_sonar') {
       sh 'mvn clean verify sonar:sonar'
    }
  }
   stage('testing'){
        echo 'todo : tesing code with maven and generator report'
   }

   stage('building') {
      if (isUnix()) {
         echo 'maven building in Linux'
         sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
      } else {
         echo 'maven building in Windows'
         bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
      }
   }

    stage('package') {/
    

        echo 'package with maven'
    }

   stage('deploy to nexus') {
        echo 'deploy to nexus with maven'
   }

   stage('SAT') {
        echo '集成测试'
   }

   stage('UAT') {
        echo '验收测试'
   }

   stage('PROD') {
        echo  'release to Production '
   }
}