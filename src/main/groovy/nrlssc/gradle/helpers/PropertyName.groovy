package nrlssc.gradle.helpers

import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

enum PropertyName {
    artiURL("artiURL"),
    gitlabURL("gitlabURL"),
    hgCommandPath("hgCommandPath"),
    resolveRemote("remote_repos", "resolveRemote"),
    fastBuild("fast_build", "fastBuild"),

    disconnected('disconnected'),
    graphVizPath('graphVizPath'),
    isCI("isCI", "is_ci"),
    preferCoprojectSubdir('preferCoprojectSubdir'),
    gitCommandPath('gitCommandPath'),
    
    artiUsername('artiUsername'),
    artiPassword('artiPassword'),
    artiPublishUsername("artiPublishUsername"),
    artiPublishPassword("artiPublishPassword"),
    gitlabUsername('gitlabUsername'),
    gitlabPassword('gitlabPassword'),
    gitlabPublishUsername('gitlabPublishUsername'),
    gitlabPublishPassword('gitlabPublishPassword'),
    
    resolveArti('resolveArti', 'resolveArtifactory'),
    resolveGitlab('resolveGitlab'),
    publishArti('publishArti', 'publishArtifactory'),
    publishGitlab('publishGitlab'),
    groupCode('groupCode')


    private static Logger logger = LoggerFactory.getLogger(PropertyName.class)
    private List<String> names = new ArrayList<>()
    PropertyName(String... names){
        for(String s : names){
            this.names.add(s)
        }
    }

    boolean getAsBoolean(Project project)
    {
        return getAsBoolean(project, names)
    }
    
    static boolean getAsBoolean(Project project, String... names)
    {
        return getAsBoolean(project, names.toList())
    }
    
    static boolean getAsBoolean(Project project, List<String> names){
        boolean retVal = false
        for(String name : names){
            if(project.hasProperty(name)){
                retVal = project.property(name).toString().toBoolean()
                break
            }
        }
        return retVal
    }
    
    
    String getAsString(Project project)
    {
        return getAsString(project, names)
    }

    static String getAsString(Project project, String... names)
    {
        return getAsString(project, names.toList())
    }
    
    static String getAsString(Project project, List<String> names){
        String retVal = ''
        for(String name : names){
            if(project.hasProperty(name)){
                retVal = project.property(name)
                break
            }
        }
        return retVal
    }

    boolean exists(Project project)
    {
        return exists(project, names)
    }
    
    static boolean exists(Project project, String... names)
    {
        return exists(project, names.toList())    
    }
    
    static boolean exists(Project project, List<String> names){
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