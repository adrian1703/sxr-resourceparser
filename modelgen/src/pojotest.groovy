import groovy.xml.XmlParser
import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil
import groovy.xml.slurpersupport.GPathResult


/* ========== template attributes =============== */
String name
String parent
String term = ''
String type = ''
int count = 0

/* ========== helper attributes   =============== */
Node        root
/* ========== script execution    =============== */
root   = Utils.readXml("ubl-invoice.xml")
new Node(root.Document[0], 'Name', 'INVOICE')
def elements = root.Document.'*'
List basicAttribs = []
List complexAttribs = []
Utils.fillLists(elements, basicAttribs, complexAttribs)

def createPojoFile(Node node, Map data){
    String packageName, template, className
    List complexAttribs = []
    List basicAttribs   = []
    className   = data.className
    if(className == '')
        println data
    packageName = 'sxr.model'
    Utils.fillLists(node.'*', basicAttribs, complexAttribs)


    template = """package $packageName;
${complexAttribs.collect{"import $packageName.${it.className};"}.join("\n")}

public class $className {
${basicAttribs.collect{"\tprivate String ${it.attribName};"}.join('\n')}
${complexAttribs.collect{"\tprivate ${it.className} ${it.attribName};"}.join('\n')}
}
"""

    Utils.writeToFile template, className

    complexAttribs.each {
        if(it.node == null)
            return
        createPojoFile(it.node, it)
    }
}

createPojoFile(root.Document[0] as Node, [className : 'Invoice'] as Map)

return 'success'
