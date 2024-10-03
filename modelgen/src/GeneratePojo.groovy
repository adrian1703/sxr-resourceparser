import static main.FileRWD.*
import static main.Utils.fillLists
import static main.java.JavaUtils.createJavaTemplate
import static main.java.JavaUtilsStatic.createCodeLists

/* ========== helper attributes   =============== */
Node        root
/* ========== script execution    =============== */
String path        = args.length > 0 ? args[0] : "./../../resources/peppol-bis-invoice-3/structure/syntax"
String outputDir   = args.length > 1 ? args[1] : "./../../sxr-javamodel/src/sxr/invoice/ubl"
String rootFile    = "ubl-invoice.xml"
String packageName = 'sxr.invoice.ubl'
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


deleteDirectory(outputDir)
createCodeLists(packageName + ".codes", outputDir + "/codes", "$path/../codelist")
createPojoFile(root.Document[0] as Node,
               [className: 'Invoice', type:'Invoice.class', order: -1] as Map,
               packageName + ".entities", outputDir + "/entities",
               known)
println known
println "path: $path"
println "output: $outputDir"
return 'success'
