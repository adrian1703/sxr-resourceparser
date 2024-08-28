import static Utils.*
import static JavaUtils.*

/* ========== helper attributes   =============== */
Node        root
/* ========== script execution    =============== */
String path = args.length > 0 ? args[0] : "./../../resources/peppol-bis-invoice-3/structure/syntax"
String rootFile = "ubl-invoice.xml"
String packageName = 'sxr.model'
root   = readXml(path, rootFile)

static def createPojoFile(Node node, Map data, String packageName){
    String template, className
    List basicProperties   = []
    List complexProperties = []
    className           = data.className

    fillLists(node.'*', basicProperties, complexProperties)
    template = createJavaTemplate(packageName, className, basicProperties, complexProperties)
    writeToFile template, className

    complexProperties.each {
        if(it.node == null)
            return
        createPojoFile(it.node as Node, it as Map, packageName)
    }
}



String annotationString = createXmlAttribInterface(packageName)
writeToFile(annotationString, "XmlAttribute.java")
createPojoFile(root.Document[0] as Node, [className : 'Invoice'] as Map, packageName)

return 'success'
