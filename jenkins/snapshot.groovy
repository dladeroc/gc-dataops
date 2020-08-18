
String version=""
String timeStamp=""

node {
    try{
        stage('Prepare') {
            checkout scm
            version=steps.sh(script: "cat version.txt", returnStdout: true).trim()
            timeStamp = steps.sh(script: "date '+%Y%m%d%H%M%S'", returnStdout: true).trim();
        }
        stage('build') {
            steps.echo "packaging sources"
            steps.sh "tar czvf liquibase.${timeStamp}.tar.gz src"
        }
        stage('publish results'){
            steps.archiveArtifacts(artifacts:'**/*.gz')
        }
        stage('deploy') {
            String credentialId="user-password-database-azure"
            String liquibaseImage="gc/liquibase-4.0.0:1.0"
            steps.withCredentials([
                 [$class: "UsernamePasswordMultiBinding", credentialsId: credentialId, usernameVariable: 'databaseUser', passwordVariable: 'databasePassword']
                 ]) {
                     try{
                         String liquibaseExec="liquibase --url='jdbc:sqlserver://liquibase.database.windows.net:1433;database=liquibase_db;' --changeLogFile=/src/changelog.xml --username='${env.databaseUser}' --password='${env.databasePassword}' --logLevel=info update"

                         steps.echo "liquibaseExec: ${liquibaseExec}"
                         steps.sh "docker run --rm --network=host -v '${env.WORKSPACE}:/home/liquibase/'  ${liquibaseImage} ${liquibaseExec}"
                    } catch (Exception e) {
                         throw e
                    }
            }
        }

        stage('..::SnapShot::..') {
            String credentialId="user-password-database-azure"
            String liquibaseImage="gc/liquibase-4.0.0:1.0"
            steps.withCredentials([
                 [$class: "UsernamePasswordMultiBinding", credentialsId: credentialId, usernameVariable: 'databaseUser', passwordVariable: 'databasePassword']
                 ]) {
                     try{
                         String liquibaseExec="liquibase --url='jdbc:sqlserver://liquibase.database.windows.net:1433;database=liquibase_db;' --changeLogFile=/src/changelog.xml --username='${env.databaseUser}' --password='${env.databasePassword}' --logLevel=info snapshot"

                         steps.echo "liquibaseExec: ${liquibaseExec}"
                         steps.sh "docker run --rm --network=host -v '${env.WORKSPACE}:/home/liquibase/'  ${liquibaseImage} ${liquibaseExec}"
                    } catch (Exception e) {
                         throw e
                    }
            }
        }


    } catch(Exception e) {
        throw e
    }
}