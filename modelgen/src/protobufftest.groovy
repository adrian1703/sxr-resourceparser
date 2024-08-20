import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil
import groovy.xml.slurpersupport.GPathResult


/* ========== template attributes =============== */
String name
String type
int count = 1

/* ========== script execution    =============== */
XmlSlurper sluper = new XmlSlurper(false, false) //using the sluper because that way we don't need to deal with namespaces
Node node         = Utils.readXml("/part/delivery.xml")
GPathResult root  = sluper.parseText(XmlUtil.serialize(node))
type              = root.Name.text().replace(" ", "_")

List elements =  root.'**'.findAll { it.Reference.@type == "BUSINESS_TERM" }

String attribString = elements.collect {
    name = (it.Name as String).replace(" ", "_")
    return "\t string $name = ${count++};"
}.join("\n")

String template =
        """syntax = "proto3";
package com.example;

message $type {
$attribString
}
"""
println template
def file = new File("./../proto-model/test.proto")
file.write template


