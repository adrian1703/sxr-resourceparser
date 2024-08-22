import groovy.xml.XmlParser
import groovy.namespace.QName
import groovy.xml.XmlUtil
import groovy.xml.slurpersupport.GPathResult

/* only work with patch-2 of my fork */
class Utils {

    static Node readXml(String fileName) {
        String path = "./../../resources/peppol-bis-invoice-3/structure/syntax"
        String rootFile = "$path/$fileName"
        XmlParser parser = new XmlParser(namespaceAware: false)
        Node root = parser.parse(rootFile)
        //resolving includes
        List<Node> includeNodes = root.'**'.findAll{ Node node ->
            def name = node.name()
            return (name instanceof QName &&
                    name.localPart == 'Include')
        }
        includeNodes.each { Node node ->
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

}