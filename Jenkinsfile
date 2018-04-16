node {
   def mvnHome
   stage('敏宝宝全世界最漂亮') {
      git 'git@github.com:zach007/Chat.git'
      echo 'check out from git@github.com:zach007/Chat.git'
      mvnHome = tool 'maven_3.5'
   }
   stage('敏宝宝有时候很凶') {
      if (isUnix()) {
         echo 'maven building in Linux'
         sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
      } else {
         echo 'maven building in Windows'
         bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
      }
   }
}