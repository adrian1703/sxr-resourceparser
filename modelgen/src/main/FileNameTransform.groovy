package main

class FileNameTransform {

    static String convertToCamelCase(String text, boolean useTermText) {
        if(useTermText)
            return text.capitalize()
        else convertToCamelCase(text)
    }

    static String convertToCamelCase(String text) {
        def words = text.split("[ .,/:]+")
        return words.collect { it.replace("'", "").toLowerCase().capitalize() }
                .join()
                .uncapitalize()
    }

    static String convertToPascalCase(String text, boolean useTermText) {
        if(useTermText)
            return text.capitalize()
        else convertToPascalCase(text)
    }

    static String convertToPascalCase(String text) {
        def words = text.split("[ .,/:]+")
        return words.collect { it.replace("'", "").toLowerCase().capitalize() }
                .join()
    }
}