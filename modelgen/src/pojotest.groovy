import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil
import groovy.xml.slurpersupport.GPathResult


/* ========== template attributes =============== */
String name
String term
String type
int count = 1

/* ========== script execution    =============== */
XmlSlurper sluper = new XmlSlurper(false, false) //using the sluper because that way we don't need to deal with namespaces
Node node         = Utils.readXml("ubl-invoice.xml")
GPathResult root  = sluper.parseText(XmlUtil.serialize(node))

def elements = Utils.getElementsWithDataTypes(root)
elements.each {
    name = it.Name
    term = it.Term
    type = it.DataType
    println([name, term, type])
}
println elements.size()



