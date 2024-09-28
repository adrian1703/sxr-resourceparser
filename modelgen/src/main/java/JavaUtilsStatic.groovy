package main.java

import main.FileNameTransform
import main.FileRWD

public class JavaUtilsStatic {
    static createXmlAttributeInterface(String packageName, String out) {
        String template = """package $packageName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Repeatable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(XmlAttributes.class)
public @interface XmlAttribute {
    String term();
    String btRef();
    Class  type();
    String parent();
}
"""
        FileRWD.writeToFile(template, out, "XmlAttribute")
    }

    static createXmlAttributesInterface(String packageName, String out) {
        String template = """package $packageName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlAttributes {
    XmlAttribute[] value();
}
"""
        FileRWD.writeToFile(template, out, "XmlAttributes")
    }

    static createXmlElementInterface(String packageName, String out) {
        String template = """package $packageName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface XmlElement {
    String  term();
    String  btRef();
    Class   type();
    int     order();
    int     min();
    int     max();
}
"""
        FileRWD.writeToFile(template, out, "XmlElement")
    }

    static createSxrObject(String packageName, String out) {
        String template = """package $packageName;

public abstract class SxrObject {
    long  sxrId;
    
    public void setSxrId(long id) { sxrId = id; }
    
    public long getSxrId() { return sxrId; }
}
"""
        FileRWD.writeToFile(template, out, "SxrObject")
    }

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
        String template ="""package $packageName;

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
