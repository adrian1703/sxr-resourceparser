//file:noinspection GroovyAssignabilityCheck
import groovy.xml.XmlParser
import groovy.namespace.QName
/* only work with patch-2 of my fork */

String path = "./../../resources/peppol-bis-invoice-3/structure/syntax"
String rootFile = "$path/ubl-invoice.xml"
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

List<Node> references = root.'**'.findAll {Node node ->
    def name = node.name()
    return (name instanceof QName &&
            name.localPart == 'Reference' &&
            node.'@type' == 'BUSINESS_TERM')
}
references.sort  { Node a, Node b ->
    String atext = a.text().split(',')[0]
    String btext = b.text().split(',')[0]
    def prefixCompare = atext.substring(0, 2) <=> btext.substring(0, 2)
    if (prefixCompare == 0) {
        atext.replaceAll("\\D", "").toInteger() <=> btext.replaceAll("\\D", "").toInteger()
    } else {
        prefixCompare
    }
}
println(references.size())
references.each {println it.text()}