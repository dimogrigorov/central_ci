job("createAllTeamsMultiBranchScanJobs") {
    label('master')
    authorization {
        permissions('admin', [
            'hudson.model.Item.Build',
            'hudson.model.Item.Cancel',
            'hudson.model.Item.Configure',
            'hudson.model.Item.Delete',
            'hudson.model.Item.Discover',
            'hudson.model.Item.Read',
            'hudson.model.Item.Workspace',
            'hudson.model.Run.Delete',
            'hudson.model.Run.Update',
            'hudson.scm.SCM.Tag'
        ])
    }

    scm {
        git {
            remote {
                name('generateMultibranchPipelineJobs')
                url('https://grigorov@repository.secure.ifao.net:7443/scm/cicd/central-ci.git')
                credentials('grigorov')
            }
            branch('master')
        }
    }

    steps {
        dsl(['multibranch/createMultibranchJobsForAllTeams.groovy'], 'IGNORE')
    }
}
