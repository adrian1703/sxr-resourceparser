import groovy.xml.XmlParser
import groovy.namespace.QName
import groovy.xml.XmlUtil

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
    static void fillLists(elements, List basicProperties, List complexProperties) {
        elements.each {
            if (it.name() != 'Element')
                return
            String termText     = it.Term.text()
            boolean useTermText = it.Name.text() == ''
            String nameText     = useTermText ? termText[4..-1] : it.Name.text()
            Map data = [
                    name       : nameText,
                    className  : convertToPascalCase(nameText, useTermText),
                    propName   : convertToCamelCase(nameText, useTermText),
                    parent     : it.parent().Name.text(),
                    term       : termText,
                    type       : it.DataType.text(),
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

    static void writeToFile(String template, String className) {
        File file
        file = new File("./sxr/model")
        file.mkdirs()
        file = new File("./sxr/model/${className}.java")
        if(!file.exists())
            file.createNewFile()
        file.write template
    }
}