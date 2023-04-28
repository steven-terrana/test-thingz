@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

def workDir = SEED_JOB.getWorkspace()
def config = new Yaml().load(("${workDir}/jobs.yaml" as File).text)

config.systems.each{ system -> 
  folder(system.name)
  system.components.each{ component ->
    multibranchPipelineJob("${system.name}/${component.name}") {
      branchSources {
        branchSource {
          source {
            github {
              // id('23232323') // IMPORTANT: use a constant and unique identifier
              repoOwner(system.org)
              repository(component.name)
              credentialsId(config.github.credential)
              buildForkPRHead(true)
              configuredByUrl(false)
              repositoryUrl("https://github.com/${system.org}/${component.name}")
              traits {
                gitHubBranchDiscovery {
                  strategyId(3)
                }
                gitHubPullRequestDiscovery {
                  strategyId(3)
                }
              }
            }
          }
        }
      }
      factory {
        templateBranchProjectFactory {
          configurationPath('pipeline_config.groovy')
          scriptPath('Jenkinsfile')
          filterBranches(false)
        }
      }
    }
  }
}
