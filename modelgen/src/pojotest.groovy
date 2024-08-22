import groovy.xml.XmlParser
import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil
import groovy.xml.slurpersupport.GPathResult


/* ========== template attributes =============== */
String name
String parent
String term = ''
String type = ''
int count = 1

/* ========== helper attributes   =============== */
Node        root

/* ========== script execution    =============== */
root   = Utils.readXml("ubl-invoice.xml")
new Node(root.Document[0], 'Name', 'INVOICE')
def elements = root.Document.'*'
elements.each {
    name   = it.Name.text()
    parent = it.parent().Name.text()
    term   = it.Term.text()
    type   = it.DataType.text()
    println([parent, name, term, type, it.name()])
}
println elements.size()
//println XmlUtil.serialize(root)


