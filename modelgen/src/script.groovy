import groovy.xml.XmlParser
import groovy.namespace.QName

String path = "./../../resources/peppol-bis-invoice-3/structure/syntax/ubl-invoice.xml"
XmlParser parser = new XmlParser(namespaceAware: false)
Node root = parser.parse(path)
List<Node> references = root.'**'.findAll {Node node ->
    def name = node.name()
    return (name instanceof QName &&
            name.localPart == 'Reference' &&
            node.'@type' == 'BUSINESS_TERM')
}
references.sort  { Node a, Node b ->
    String atext = a.text()
    String btext = b.text()
    def prefixCompare = atext.substring(0, 2) <=> btext.substring(0, 2)
    if (prefixCompare == 0) {
        atext.replaceAll("\\D", "").toInteger() <=> btext.replaceAll("\\D", "").toInteger()
    } else {
        prefixCompare
    }
}
println(references.size())
references.each {println it.text()}