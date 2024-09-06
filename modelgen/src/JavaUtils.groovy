class JavaUtils {

    static String createJavaTemplate(String packageName, Map data, List basicProperties, List complexProperties) {
        String className
        className = data.className

        String template = """package $packageName;
import sxr.model.interfaces.*;
import java.util.List;

${annotateXmlElement(data as Map)}
public class $className {
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
            s += "\t${annotateXmlElement(prop as Map)}\n"
            prop.attributes.each { attrib ->
                s += "\t@XmlAttribute( term = \"${attrib.term}\" )" + "\n"
            }
            s += "\tpublic ${evalCardProperty("String", prop.card)} ${prop.propName};" + "\n"
        }
        return s
    }

    static String createComplexProperties(List complexProperties) {
        String s = ""
        complexProperties.each { prop ->
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

        String s = """@XmlElement(term = "${data.term}", btRef = "${data.btRef}", order = ${data.order}, min = $min, max = $max )"""
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
    int     order();
    int     min();
    int     max();
}
"""
        Utils.writeToFile(template, out,"XmlElement")
    }
}

