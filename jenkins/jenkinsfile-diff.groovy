
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
        
        stage('..::DIFF::..') {
            String credentialId="user-password-database-azure"
            String liquibaseImage="gc/liquibase-4.0.0:1.0"
            steps.withCredentials([
                 [$class: "UsernamePasswordMultiBinding", credentialsId: credentialId, usernameVariable: 'databaseUser', passwordVariable: 'databasePassword']
                 ]) {
                     try{
                         String liquibaseExec="liquibase --outputFile=mydiff.txt --url='jdbc:sqlserver://liquibase.database.windows.net:1433;database=liquibase_dev;' --changeLogFile=/src/changelog.xml --username='${env.databaseUser}' --password='${env.databasePassword}' --logLevel=info --referenceUrl='jdbc:sqlserver://liquibase.database.windows.net:1433;database=liquibase_cert;' --referenceUsername='${env.databaseUser}' --referencePassword='${env.databasePassword}' diff"

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