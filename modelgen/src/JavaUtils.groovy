class JavaUtils {

    static String createJavaTemplate(String packageName, String className, List basicProperties, List complexProperties) {
        String template = """package $packageName;
import sxr.model.*;

public class $className {
\t/* =========== Basic Properties   =========== */
${createJavaBasicProperties(basicProperties)}
\t/* =========== Complex Properties =========== */
${complexProperties.collect { "\tprivate ${it.className} ${it.propName};" }.join('\n')}
}
"""
        return template
    }

    static String createJavaBasicProperties(List basicProperties) {
        String s = ""
        basicProperties.each { prop ->
            prop.attributes.each { attrib ->
                s += "\t@XmlAttribute( term = \"${attrib.term}\" )" + "\n"
            }
            s += "\tprivate String ${prop.propName};" + "\n"
        }
        return s
    }

    static String createXmlAttribInterface(String packageName) {
        return """package $packageName;
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

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlAttributes {
    XmlAttribute[] value();
}"""
    }
}
