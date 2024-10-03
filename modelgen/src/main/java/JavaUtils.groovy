package main.java

class JavaUtils {

    static String createJavaTemplate(String packageName, Map data, List basicProperties, List complexProperties) {
        String className
        className = data.className
        data.order = -1
        String template = """/*********** GENERATED - DO NOT MODIFY ***********/
package $packageName;
import sxr.model.entities.SxrObject;
import sxr.model.interfaces.*;
import sxr.invoice.ubl.codes.*;
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
            prop.attributes.each { attrib ->
                s += "\t@XmlAttribute( term = \"${attrib.term}\", btRef = \"${attrib.bt}\", type = ${convertConstructorType(convertDataType(attrib))}, parent = \"${prop.propName}\")" + "\n"
                s += "\tpublic ${convertDataType(attrib)} ${attrib.name};" + "\n"
            }
            s += "\t${annotateXmlElement(prop as Map)}\n"
            s += "\tpublic ${evalCardProperty(type, prop.card)} ${prop.propName};" + "\n"
        }
        return s
    }

    static String convertDataType(Map prop) {
        Map typeMap = [
                default    : 'String',
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
            return JavaUtilsStatic.getCodeClassName((String) prop.codetype)

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
}

