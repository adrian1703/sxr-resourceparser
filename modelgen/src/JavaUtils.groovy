class JavaUtils {

    static String createJavaTemplate(String packageName, Map data, List basicProperties, List complexProperties) {
        String className
        className = data.className
        data.order = -1
        String template = """package $packageName;
import sxr.model.entities.SxrObject;
import sxr.model.interfaces.*;
import sxr.model.codes.*;
import java.util.List;

${annotateXmlElement(data as Map)}
public class $className extends SxrObject {
\t/* =========== Basic Properties   =========== */
${createJavaBasicProperties(basicProperties)}
\t/* =========== Complex Properties =========== */
${createComplexProperties(complexProperties)}
}
"""
        return template
    }

    static String createJavaBasicProperties(List basicProperties) {
        String s = ""
        basicProperties.each { prop ->
            String type = convertDataType(prop)
            prop.type   = convertConstructorType(type)
            s += "\t${annotateXmlElement(prop as Map)}\n"
            prop.attributes.each { attrib ->
                s += "\t@XmlAttribute( term = \"${attrib.term}\" )" + "\n"
            }
            s += "\tpublic ${evalCardProperty(type, prop.card)} ${prop.propName};" + "\n"
        }
        return s
    }

    static String convertDataType(Map prop) {
        Map typeMap = [
                default    : 'null',
                Text       : 'String',
                Boolean    : 'boolean',
                Identifier : 'String',
                Amount     : 'float' ,
                Percentage : 'int',
                Quantity   : 'int',
                Date       : 'java.util.Date',
                "Binary object" : 'byte[]',
                "Document Reference" : 'String'
        ]
        String type = typeMap.getOrDefault(prop.type, null);

        if (type != null)
            return type

        if (prop.type == "Code" && prop.codetype != null)
            return getCodeClassName((String) prop.codetype)

        type = switch (prop.term) {
            case "cbc:ChargeIndicator" -> "boolean"
            case "cbc:ID" -> "String"
            case "cbc:DocumentTypeCode" -> "String"
            default -> typeMap.get("default") // we should never get to this
        }
        return type
    }

    static String convertConstructorType(String type){
        Map typeMap = [
                'null'          : 'Object.class',
                'String'        : 'String.class',
                'boolean'       : 'Boolean.class',
                'float'         : 'Float.class',
                'int'           : 'Integer.class',
                'java.util.Date': 'java.util.Date.class',
                'byte[]'        : 'byte[].class' //TODO: Later
        ]
        return typeMap[type]?: type + ".class"
    }

    static String createComplexProperties(List complexProperties) {
        String s = ""
        complexProperties.each {Map prop ->
            prop.type = prop.className + ".class"
            s += "\t${annotateXmlElement(prop as Map)}\n"
            prop.attributes.each { attrib ->
                s += "\t@XmlAttribute( term = \"${attrib.term}\" )" + "\n"
            }
            s += "\tpublic ${evalCardProperty(prop.className, prop.card)} ${prop.propName};" + "\n"
        }
        return s
    }
    static String evalCardProperty(String type, String card) {
        String s = type
        if(card?.matches("\\d+\\.\\.n"))
            s = "List<$s>"
        return s
    }

    static String annotateXmlElement(Map data) {
        String cardinality = data.card
        int min, max
        if (cardinality == null){
            min = 1
            max = 1
        } else {
            def vals = cardinality.split('\\.\\.')
            min = vals[0] as int
            max = vals[1] == 'n' ? -1 : vals[1] as int
        }

        String s = """@XmlElement(term = "${data.term}", btRef = "${data.btRef}", type = ${data.type}, order = ${data.order}, min = $min, max = $max )"""
        return s
    }

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
}
"""
        Utils.writeToFile(template, out, "XmlAttribute")
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
        Utils.writeToFile(template, out,"XmlAttributes")
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
        Utils.writeToFile(template, out,"XmlElement")
    }

    static createSxrObject(String packageName, String out) {
        String template = """package $packageName;

public abstract class SxrObject {
    long  sxrId;
    
    public void setSxrId(long id) { sxrId = id; }
    
    public long getSxrId() { return sxrId; }
}
"""
        Utils.writeToFile(template, out,"SxrObject")
    }

    static getCodeClassName(String identifier) {
        return Utils.convertToPascalCase(identifier.replace("-", " "))
    }

    static createCodeLists(String packageName, String out, String pathCodeFiles) {
        File folder = new File(pathCodeFiles)
        folder.listFiles().each { File file ->
            Node xml = Utils.readXml(pathCodeFiles, file.name)
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
        Utils.writeToFile(template, out, data.filename)
    }
}

