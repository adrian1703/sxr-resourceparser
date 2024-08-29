import static Utils.*
import static JavaUtils.*

/* ========== helper attributes   =============== */
Node        root
/* ========== script execution    =============== */
String path        = args.length > 0 ? args[0] : "./../../resources/peppol-bis-invoice-3/structure/syntax"
String outputDir   = args.length > 1 ? args[1] : "./sxr/model"
String rootFile    = "ubl-invoice.xml"
String packageName = 'sxr.model'
root               = readXml(path, rootFile)

static def createPojoFile(Node node, Map data, String packageName, String outputDir){
    String template, className
    List basicProperties   = []
    List complexProperties = []
    className              = data.className

    fillLists(node.'*', basicProperties, complexProperties)
    template = createJavaTemplate(packageName, data, basicProperties, complexProperties)
    writeToFile template, outputDir, className

    complexProperties.each {
        if(it.node == null)
            return
        createPojoFile(it.node as Node, it as Map, packageName, outputDir)
    }
}



createXmlAttributeInterface (packageName, outputDir)
createXmlAttributesInterface(packageName, outputDir)
createXmlElementInterface   (packageName, outputDir)
createPojoFile(root.Document[0] as Node, [className: 'Invoice'] as Map, packageName, outputDir)

return 'success'
