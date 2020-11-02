package nrlssc.gradle.helpers

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.publish.ivy.tasks.PublishToIvyRepository
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.net.ssl.*

class PluginUtils {
    private static Logger logger = LoggerFactory.getLogger(PluginUtils.class)

    static String execute(String command, File exeDir){
        logger.debug("Executing command: '$command'")
        def env = System.getenv().collect { k, v -> "$k=$v" }
        return command.execute(env, exeDir).getText().trim()
    }

    static String execute(List command, File exeDir){
        logger.debug("Executing command: '$command'")
        def env = System.getenv().collect { k, v -> "$k=$v" }
        return command.execute(env, exeDir).getText().trim()
    }

    static boolean folderIsEmpty(File tempFolder)
    {
        boolean retVal = true
        if(tempFolder.exists() && tempFolder.isDirectory())
        {
            tempFolder.eachFileRecurse {depRec ->
                if(depRec.isFile() && depRec.exists())
                {
                    retVal = false
                }
            }

        }
        return retVal
    }

    private static Class[] distClasses = [
            AbstractArchiveTask.class, AbstractPublishToMaven.class,
            PublishToIvyRepository.class, // ArtifactoryTask.class, DeployTask.class,
            Upload.class
    ]

    static boolean containsArtifactTask(Project proj){
        try {
            for (Task tsk : proj.gradle.taskGraph.getAllTasks()) {
                for (Class c : distClasses) {
                    if (c.isAssignableFrom(tsk.class)) {
                        return true
                    }
                    if(tsk.name.equals('artifactoryPublish') || tsk.name.equals('publish'))
                    {
                        return true
                    }
                }
            }
        }catch(IllegalStateException iex){
            return false //contains no tasks
        }

        return false
    }
    
    
    //region version
    int compareVersions(String ver1, String ver2)
    {
        int result = 0
        String[] splits = ver1.split("\\.")
        String[] splits2 = ver2.split("\\.")

        int searchLen = splits.length > splits2.length ? splits2.length : splits.length
        if(ver1.equals(ver2))
        {
            result = 0
        }
        else {
            for (int i = 0; i < searchLen; i++) {
                try {
                    int split1 = splits[i].replaceAll(/[a-zA-Z]/, '').toInteger()
                    int split2 = splits2[i].replaceAll(/[a-zA-Z]/, '').toInteger()
                    if (split1 > split2) {
                        result = -1
                        break
                    } else if (split1 < split2) {
                        result = 1
                        break
                    } else {
                        continue
                    }
                }catch(Exception ex){return 0}
            }
        }
        return result
    }
    //endregion
    
    static void ConfigureTrustAllSSL(){
        
        def nullTrustManager = [
                checkClientTrusted: { chain, authType ->  },
                checkServerTrusted: { chain, authType ->  },
                getAcceptedIssuers: { null }
        ]

        def nullHostnameVerifier = [
                verify: { hostname, session -> true }
        ]
        SSLContext sc = SSLContext.getInstance("SSL")
        sc.init(null, [nullTrustManager as X509TrustManager] as TrustManager[], null)

        SSLSocketFactory trustySocketFactory = sc.getSocketFactory()
        HostnameVerifier trustyVerifier = nullHostnameVerifier as HostnameVerifier
        HttpsURLConnection.setDefaultSSLSocketFactory(trustySocketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier(trustyVerifier)
    }


}
