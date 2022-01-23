import java.util.logging.LogManager
import static groovy.io.FileType.FILES
import jenkins.model.Jenkins

new File("${WORKSPACE}/multibranch/ifao").eachFileRecurse(FILES) {
    if(it.name.endsWith('.properties')) {
        it.withReader { r ->
            /* Loading java.util.Properties as defaults makes empty Properties object */
            def props = new Properties()
            props.load(r)
            props.each { key, value ->
                if (value == null) {
		// #logger("exception on something occurred "+e,e)
                    System.exit(0)
                }
            }

	    folder("${props.permissionsUser}TeamFolder") {
 	    	authorization {
                	configure {
                		def author = it / 'properties' / 'com.cloudbees.hudson.plugins.folder.properties.AuthorizationMatrixProperty' 
                        	author << inheritanceStrategy(class: 'org.jenkinsci.plugins.matrixauth.inheritance.NonInheritingStrategy') {
				}
			}

	            	permissions("${props.permissionsUser}", [
	    	  		"com.cloudbees.plugins.credentials.CredentialsProvider.Create",
		      		"com.cloudbees.plugins.credentials.CredentialsProvider.Delete",
		      		"com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains",
		      		"com.cloudbees.plugins.credentials.CredentialsProvider.Update",
		      		"com.cloudbees.plugins.credentials.CredentialsProvider.View",
		      		"hudson.model.Item.Build",
		      		"hudson.model.Item.Cancel",
		      		"hudson.model.Item.Create",
		      		"hudson.model.Item.Discover",
		      		"hudson.model.Item.Move",
		      		"hudson.model.Item.Read",
		      		"hudson.model.Item.Workspace",
		      		"hudson.model.Run.Delete",
		      		"hudson.model.Run.Replay",
		      		"hudson.model.Run.Update",
		      		"hudson.model.View.Configure",
		      		"hudson.model.View.Create",
		      		"hudson.model.View.Delete",
		      		"hudson.model.View.Read",
		      		"hudson.scm.SCM.Tag"
			])}
            }

            folder("${props.permissionsUser}TeamFolder/Jobs") {
              authorization {
		 permissions("${props.permissionsUser}", [
			"com.cloudbees.plugins.credentials.CredentialsProvider.Create",
			"com.cloudbees.plugins.credentials.CredentialsProvider.Delete",
			"com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains",
			"com.cloudbees.plugins.credentials.CredentialsProvider.Update",
			"com.cloudbees.plugins.credentials.CredentialsProvider.View",
			"hudson.model.Item.Build",
			"hudson.model.Item.Cancel",
			"hudson.model.Item.Configure",
			"hudson.model.Item.Create",
			"hudson.model.Item.Delete",
			"hudson.model.Item.Discover",
			"hudson.model.Item.Move",
			"hudson.model.Item.Read",
			"hudson.model.Item.Workspace",
			"hudson.model.Run.Delete",
			"hudson.model.Run.Replay",
			"hudson.model.Run.Update",
			"hudson.model.View.Configure",
			"hudson.model.View.Create",
			"hudson.model.View.Delete",
			"hudson.model.View.Read",
			"hudson.scm.SCM.Tag"
                ])}
             }

	      multibranchPipelineJob("${props.permissionsUser}TeamFolder/Jobs/${props.jobName}") {
              println "${props.jobName}"
              branchSources {
              branchSource {
                source {
                  bitbucketSCMSource {
                    repoOwner("${props.repoOwner}")
                    println "${props.repoOwner}"
                    repository("${props.repository}")
                    println "${props.repository}"
                    autoRegisterHook(true)
                    bitbucketServerUrl("${props.bitbucketServerUrl}")
                    println "${props.bitbucketServerUrl}"
                    checkoutCredentialsId("${props.checkoutCredentialsId}")
                    println "${props.checkoutCredentialsId}"
                    credentialsId("${props.checkoutCredentialsId}")
                    println "${props.checkoutCredentialsId}"
                  }
                }
              }}
  
              configure {
                  def traits = it / sources / data / 'jenkins.branch.BranchSource' / source / traits
                  def branchSource = it / sources / data / 'jenkins.branch.BranchSource'
      
                  if ("${props.allowBranchDiscovery}" == "true") {
                      traits << 'com.cloudbees.jenkins.plugins.bitbucket.BranchDiscoveryTrait' {
                          strategyId(1)
                      }
                  }
      
                  if ("${props.allowOriginPullRequestDiscovery}" == "true") {
                      traits << 'com.cloudbees.jenkins.plugins.bitbucket.OriginPullRequestDiscoveryTrait' {
                          strategyId(1)
                      }
                  }
      
                  if ("${props.allowTagsDiscovery}" == "true") {
                      traits << 'com.cloudbees.jenkins.plugins.bitbucket.TagDiscoveryTrait' {
                      }
                  }
                   
                  if ("${props.noTriggerOnCommit}" == "true") {
                      branchSource << strategy(class: 'jenkins.branch.DefaultBranchPropertyStrategy') {
                          properties(class: 'java.util.Arrays$ArrayList') {
                              a(class: 'jenkins.branch.BranchProperty-array') << 'jenkins.branch.NoTriggerBranchProperty'{}
                          }
                      }
                  } 
              }
	   }
        }
    }
}
