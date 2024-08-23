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
List basicAttribs = []
List complexAttribs = []
Utils.fillLists(elements, basicAttribs, complexAttribs)

def createPojoFile(Node node){
    List basicAttribs = []
    List complexAttribs = []
    Utils.fillLists(node.'*', basicAttribs, complexAttribs)
    String className = Utils.convertToPascalCase node.Name.text()
    String packageName = 'sxr.model'
    String template = """package $packageName;
${complexAttribs.collect{"import $packageName.${it.className};"}.join("\n")}

public class $className {
${basicAttribs.collect{"\tprivate String ${it.attribName};"}.join('\n')}
${complexAttribs.collect{"\tprivate ${it.className} ${it.attribName};"}.join('\n')}
}
"""
//    println template
    File file
    file = new File("./sxr/model")
    file.mkdirs()
    file = new File("./sxr/model/${className}.java")
    if(!file.exists())
        file.createNewFile()
    file.write template

    complexAttribs.each {
        println it
        if(it.node == null)
            return
        createPojoFile(it.node)
    }
}

createPojoFile(root.Document)
