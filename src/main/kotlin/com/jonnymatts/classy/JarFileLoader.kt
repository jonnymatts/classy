package com.jonnymatts.classy

import de.jupf.staticlog.Log
import java.util.jar.JarFile

class JarFileLoader {

    fun load(jarFilePaths: List<String>): List<JarFile> {
        return jarFilePaths.map{jarFilePath ->
            try{
                JarFile(jarFilePath)
            } catch (e: Exception) {
                Log.error("Could not find file $jarFilePath", e)
                null
            }
        }.filterNotNull()
    }
}