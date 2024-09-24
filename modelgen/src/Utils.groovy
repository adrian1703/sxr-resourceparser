import groovy.xml.XmlParser
import groovy.namespace.QName
import groovy.xml.XmlUtil
import java.nio.file.*

/* only work with patch-2 of my fork */
class Utils {

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

    static List getElementsWithDataTypes(def node){
        return node.'**'.findAll{ it.name() == "Element" && it.DataType.text() != ""}
    }

    static String convertToCamelCase(String text, boolean useTermText) {
        if(useTermText)
            return text.capitalize()
        else convertToCamelCase(text)
    }
    static String convertToCamelCase(String text) {
        def words = text.split("[ .,/:]+")
        return words.collect { it.replace("'", "").toLowerCase().capitalize() }
                .join()
                .uncapitalize()
    }
    static String convertToPascalCase(String text, boolean useTermText) {
        if(useTermText)
            return text.capitalize()
        else convertToPascalCase(text)
    }
    static String convertToPascalCase(String text) {
        def words = text.split("[ .,/:]+")
        return words.collect { it.replace("'", "").toLowerCase().capitalize() }
                .join()
    }
    static void fillLists(elements, List basicProperties, List complexProperties, Map known) {
        int count = 0
        elements.each {
            if (it.name() != 'Element')
                return
            String termText     = it.Term.text()
            boolean useTermText = it.Name.text() == ''
            String nameText     = useTermText ? termText[4..-1] : it.Name.text()
            known[nameText]  = known.get(nameText, -1) + 1
            if (known[nameText] > 0)
                nameText = nameText + known[nameText]
            Map data = [
                    name       : nameText,
                    className  : convertToPascalCase(nameText, useTermText),
                    propName   : convertToCamelCase(nameText, useTermText),
                    btRef      : it.Reference
                            .find { it.@type == "BUSINESS_TERM" }
                            ?.text()?.trim(),
                    card       : it.@cardinality,
                    order      : count++,
                    parent     : it.parent().Name.text(),
                    term       : termText,
                    type       : it.DataType.text(),
                    codetype   : it.Reference
                            .find { it.@type == "CODE_LIST" }
                            ?.text()?.trim(),
                    node       : it,
                    attributes : []
            ]
            for(Node attributes :  it.Attribute) {
                Map attrib = [term : attributes.Term.text()]
                data.attributes.add(attrib)
            }

            boolean isLeaf = it.Element.isEmpty()
            def addTo = isLeaf ? basicProperties : complexProperties
            addTo << data
        }
    }

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
}