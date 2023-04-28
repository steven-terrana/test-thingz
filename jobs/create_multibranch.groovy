@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

def workDir = SEED_JOB.getWorkspace()
def config = new Yaml().load(("${workDir}/jobs.yaml" as File).text)

config.folders.each{ folder_name -> 
  folder(folder_name)
}
