// Tests, builds and deploys the MyHouse backend (my-house-backend), which runs the house's lights and
// furnace schedules. The job (triggers, including the daily 07:22 UTC run, and whether it's enabled)
// is defined in do-k8s-cluster/jenkins/casc/jobs/apps.groovy, and the ci* steps come from that
// repo's jenkins/shared-library.
@Library('ci') _

pipeline {
    agent any
    options {
        timeout(time: 30, unit: 'MINUTES')
    }
    stages {
        stage('Test and build jar') {
            steps {
                // docker: true because the database tests start Postgres with testcontainers. The
                // newest Maven image on Java 18 (what production runs) is 3.8.6.
                ciInContainer('maven:3.8.6-eclipse-temurin-18', docker: true) {
                    // Java takes its home from the user database, where the jenkins uid has no
                    // entry inside the container, so it would use a folder named "?" in the
                    // workspace. JAVA_TOOL_OPTIONS reaches every JVM, including the test JVMs
                    // Maven forks (testcontainers keeps its settings in the home folder).
                    withEnv(["JAVA_TOOL_OPTIONS=-Duser.home=${env.WORKSPACE_TMP}/home"]) {
                        // Every plugin is in pluginManagement, so pmd and shade run only when
                        // named. package runs the tests once (CI-2's `test ... package` ran them twice).
                        sh 'mvn -B -ntp clean package pmd:check shade:shade'
                    }
                }
                sh 'cp target/MyHouse-0.0.1-SNAPSHOT.jar MyHouse.jar'
            }
        }
        stage('Build image') {
            steps {
                script {
                    env.IMAGE_LABEL = ciImage(repo: 'my-house', variant: 'backend')
                }
            }
        }
        stage('Deploy') {
            steps {
                ciDeploy(release: 'my-house-backend', chart: 'deploy/my-house-backend', imageLabel: env.IMAGE_LABEL)
            }
        }
    }
    post {
        failure { ciNotify() }
        fixed { ciNotify() }
    }
}
