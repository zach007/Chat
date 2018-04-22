node {
   def mvnHome
   stage('checkout') {
      git 'git@github.com:zach007/Chat.git'
      echo 'check out from git@github.com:zach007/Chat.git'
      mvnHome = tool 'maven_3.5'
   }

   stage('SonarQube analysis') {
       // requires SonarQube Scanner 2.8+
       def scannerHome = tool 'SonarQube Scanner 2.8';
       withSonarQubeEnv('My SonarQube Server') {
         sh "${scannerHome}/bin/sonar-scanner"
       }
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
}