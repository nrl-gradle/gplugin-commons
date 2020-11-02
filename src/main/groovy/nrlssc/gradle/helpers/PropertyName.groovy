package nrlssc.gradle.helpers

import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

enum PropertyName {
    artiURL("artiURL"),
    nexusURL("nexusURL"),
    hgCommandPath("hgCommandPath"),
    resolveRemote("remote_repos", "resolveRemote"),
    fastBuild("fast_build", "fastBuild"),
    publishUsername("publishUsername"),
    publishPassword("publishPassword"),
    disconnected('disconnected'),
    graphVizPath('graphVizPath'),
    isCI("isCI", "is_ci"),
    gitCommandPath('gitCommandPath'),
    
    artiUsername('artiUsername'),
    artiPassword('artiPassword'),
    nexusUsername('nexusUsername'),
    nexusPassword('nexusPassword'),
    
    resolveArti('resolveArti', 'resolveArtifactory'),
    resolveNexus('resolveNexus'),
    publishArti('publishArti', 'publishArtifactory'),
    publishNexus('publishNexus')


    private static Logger logger = LoggerFactory.getLogger(PropertyName.class)
    private List<String> names = new ArrayList<>()
    PropertyName(String... names){
        for(String s : names){
            this.names.add(s)
        }
    }

    boolean getAsBoolean(Project project){
        boolean retVal = false
        for(String name : names){
            if(project.hasProperty(name)){
                retVal = project.property(name).toString().toBoolean()
                break
            }
        }
        return retVal
    }

    String getAsString(Project project){
        String retVal = ''
        for(String name : names){
            if(project.hasProperty(name)){
                retVal = project.property(name)
                break
            }
        }
        return retVal
    }

    boolean exists(Project project){
        boolean retVal = false
        for(String name : names){
            if(project.properties.containsKey(name)){
                retVal = true
                break
            }
        }
        return retVal
    }

}