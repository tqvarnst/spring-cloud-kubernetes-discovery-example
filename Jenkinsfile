pipeline {
  agent {
      label 'maven'
  }
  stages {
    stage('Verify') {
      steps {
        sh "cp .settings.xml ~/.m2/settings.xml"
        sh "mvn verify"
      }
    }
    stage('Build JAR') {
      steps {
        sh "cp .settings.xml ~/.m2/settings.xml"
        sh "mvn clean package -Popenshift -DskipTests"
      }
    }
    stage('Archive JAR') {
      steps {
        //sh "mvn deploy -DskipTests"
        echo "TODO: Deploy the file to the local Nexus repo"
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.startBuild("catalog", "--from-file=target/catalog-${readMavenPom().version}.jar", "--wait")
          }
        }
      }
    }
    stage('Deploy') {
      steps {
        script {
          openshift.withCluster() {
            def result, dc = openshift.selector("dc", "catalog")
            dc.rollout().latest()
            timeout(10) {
                result = dc.rollout().status("-w")
            }
            if (result.status != 0) {
                error(result.err)
            }
          }
        }
      }
    }
  }
}