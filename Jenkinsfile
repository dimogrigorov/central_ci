@Library('shared-pipeline-library')
def podLabel = "mypod-${UUID.randomUUID().toString()}"
jnlpTemplate(podLabel) {
    node(podLabel) {
        stage('Run GIT Cloning') {
            container('jnlp') {
                sh 'echo hello'
            }
        }
    }
}
