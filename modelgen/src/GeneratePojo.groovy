import static main.FileRWD.*
import static main.Utils.fillLists
import static main.java.JavaUtils.createJavaTemplate
import static main.java.JavaUtilsStatic.*

/* ========== helper attributes   =============== */
Node        root
/* ========== script execution    =============== */
String path        = args.length > 0 ? args[0] : "./../../resources/peppol-bis-invoice-3/structure/syntax"
String outputDir   = args.length > 1 ? args[1] : "./sxr/model"
String rootFile    = "ubl-invoice.xml"
String packageName = 'sxr.model'
root               = readXml(path, rootFile)
Map known          = [:]

static def createPojoFile(Node node, Map data, String packageName, String outputDir, Map known){
    String template, className
    List basicProperties   = []
    List complexProperties = []
//    known[data.className]  = known.get(data.className, -1) + 1
//    if (known[data.className] > 0)
//        data.className = data.className + known[data.className]
    className =  data.className
    fillLists(node.'*', basicProperties, complexProperties, known)
    template = createJavaTemplate(packageName, data, basicProperties, complexProperties)
    writeToFile template, outputDir, className

    complexProperties.each {
        if(it.node == null)
            return
        createPojoFile(it.node as Node, it as Map, packageName, outputDir, known)
    }
    return known
}


createXmlAttributeInterface (packageName + ".interfaces", outputDir + "/interfaces")
createXmlAttributesInterface(packageName + ".interfaces", outputDir + "/interfaces")
createXmlElementInterface   (packageName + ".interfaces", outputDir + "/interfaces")
createSxrObject             (packageName + ".entities", outputDir + "/entities")
createCodeLists             (packageName + ".codes", outputDir + "/codes", "$path/../codelist")
createPojoFile(root.Document[0] as Node,
               [className: 'Invoice', type:'Invoice.class', order: -1] as Map,
               packageName + ".entities.invoice", outputDir + "/entities/invoice",
               known)

// copy to submodule
def sourceDir = "$outputDir/../../sxr"
def targetDir = "$outputDir/../../../../sxr-javamodel/scr/sxr"

deleteDirectory(targetDir)
copyDirectory(sourceDir, targetDir)
println known
return 'success'
