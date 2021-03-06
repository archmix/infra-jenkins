def call(service,
         gitUrl,
         gitCommitEmail,
         gitCommitUsername,
         pathSources,
         folderRepository = 'developers-portal') {
    def pathMetada = "src/data/metadata/"
    sh "git clone $gitUrl $folderRepository"
    sh "mkdir -p $folderRepository/$pathMetada"
    sh "cp -r $pathSources $folderRepository/src"
    sh "git config --global user.email '$gitCommitEmail'"
    sh "git config --global user.name '$gitCommitUsername'"
    sh "cd $folderRepository && git add ."

    def exist = sh(returnStdout: true, script: "cd $folderRepository && git status -s")

    if (!"".equals(exist)) {
        sh "cd $folderRepository && git commit -m 'jenkins build $service ${env.BUILD_NUMBER}'"
        sh "cd $folderRepository && git push"
    } else {
        echo "No changes found. Skipping git commit."
    }
}