package main

import groovy.namespace.QName
import groovy.xml.XmlParser
import groovy.xml.XmlUtil

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class FileRWD {

    static void writeToFile(String template, String out, String className) {
        File file
        file = new File("$out")
        file.mkdirs()
        file = new File("$out/${className}.java")
        if(!file.exists())
            file.createNewFile()
        file.newPrintWriter("UTF-8").withWriter { writer ->
            writer << template
        }
    }

    static def deleteDirectory(String dirPath) {
        Path dir = Paths.get(dirPath)
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach { Files.delete(it) }
            println "Directory '${dirPath}' deleted successfully."
        } else {
            println "Directory '${dirPath}' does not exist."
        }
    }

    static def copyDirectory(String sourceDirPath, String targetDirPath) {
        Path sourceDir = Paths.get(sourceDirPath)
        Path targetDir = Paths.get(targetDirPath)

        Files.walk(sourceDir).forEach { sourcePath ->
            Path targetPath = targetDir.resolve(sourceDir.relativize(sourcePath))
            if (Files.isDirectory(sourcePath)) {
                Files.createDirectories(targetPath)
            } else {
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
        println "Directory '${sourceDirPath}' copied to '${targetDirPath}' successfully."
    }

    static Node readXml(String path, String fileName) {

        XmlParser parser = new XmlParser(namespaceAware: false)
        Node root = parser.parse("$path/$fileName")
        //resolving includes
        List<Node> includeNodes = root.'**'.findAll{ Node node ->
            def name = node.name()
            return (name instanceof QName &&
                    name.localPart == 'Include')
        }
        includeNodes.each {
            Node node = it as Node
            String includePath = node.text()
            Node nodeToInclude = parser.parse("$path/$includePath")
            node.replaceNode(nodeToInclude)
        }
        root = parser.parseText(XmlUtil.serialize(root)
                                       .replace(
                        'xmlns="urn:fdc:difi.no:2017:vefa:structure-1"',
                        ''))
        root.'**'.each{
            if(it instanceof Node
                    && it.children()[0] instanceof String)
                it.setValue(it.children()[0].trim())
        }
        return root
    }
}