package main


import static main.FileNameTransform.convertToCamelCase
import static main.FileNameTransform.convertToPascalCase

/* only work with patch-2 of my fork */
class Utils {

    static void fillLists(elements, List basicProperties, List complexProperties, Map known) {
        int count = 0
        elements.each {
            if (it.name() != 'Element')
                return
            String termText     = it.Term.text()
            boolean useTermText = it.Name.text() == ''
            String nameText     = useTermText ? termText[4..-1] : it.Name.text()
            known[nameText]  = known.get(nameText, -1) + 1
            if (known[nameText] > 0)
                nameText = nameText + known[nameText]
            Map data = [
                    name       : nameText,
                    className  : convertToPascalCase(nameText, useTermText),
                    propName   : convertToCamelCase(nameText, useTermText),
                    btRef      : it.Reference
                            .find { it.@type == "BUSINESS_TERM" }
                            ?.text()?.trim(),
                    card       : it.@cardinality,
                    order      : count++,
                    parent     : it.parent().Name.text(),
                    term       : termText,
                    type       : it.DataType.text(),
                    codetype   : it.Reference
                            .find { it.@type == "CODE_LIST" }
                            ?.text()?.trim(),
                    node       : it,
                    attributes : []
            ]
            for(Node attributes :  it.Attribute) {
                Map attrib = [term : attributes.Term.text()]
                data.attributes.add(attrib)
            }

            boolean isLeaf = it.Element.isEmpty()
            def addTo = isLeaf ? basicProperties : complexProperties
            addTo << data
        }
    }
}