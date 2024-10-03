package main.java

import main.FileNameTransform
import main.FileRWD

public class JavaUtilsStatic {
    static getCodeClassName(String identifier) {
        return FileNameTransform.convertToPascalCase(identifier.replace("-", " "))
    }

    static createCodeLists(String packageName, String out, String pathCodeFiles) {
        File folder = new File(pathCodeFiles)
        folder.listFiles().each { File file ->
            Node xml = FileRWD.readXml(pathCodeFiles, file.name)
            Map data = [:]
            data.filename = getCodeClassName xml.Identifier[0].text()
            data.codes = xml.Code.collect {
                [
                        id   : "P_${it.Id[0].text().replace("/", "_").replace("-", "_").replace(".", "_")}".capitalize(),
                        code : it.Id[0].text(),
                        name : it.Name[0].text()
                                .replace("\"", "")
                                .replaceAll("\\s+", " ")
                ]
            }
            createCodeEnum(data, packageName, out)
        }
    }

    static createCodeEnum(Map data, String packageName, String out) {
        String template ="""/*********** GENERATED - DO NOT MODIFY ***********/
package $packageName;

public enum ${data.filename} {
${data.codes.collect {"\t${it.id}(\"${it.code}\", \"${it.name}\")"}.join(',\n')};

\tprivate final String code;
\tprivate final String fullName;

\t${data.filename}(String code, String fullName) {
\t\tthis.code = code;
\t\tthis.fullName = fullName;
\t}

\tpublic String getCode(){
\t\treturn code;
\t}

\tpublic String getFullName(){
\t\treturn fullName;
\t}
}
"""
        FileRWD.writeToFile(template, out, data.filename)
    }
}
