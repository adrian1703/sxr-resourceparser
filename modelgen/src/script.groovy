//file:noinspection GroovyAssignabilityCheck
import groovy.xml.XmlParser
import groovy.namespace.QName
/* only work with patch-2 of my fork */

String path = "./../../resources/peppol-bis-invoice-3/structure/syntax"
String rootFile = "$path/ubl-invoice.xml"

Node root = Utils.readXml("ubl-invoice.xml")
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