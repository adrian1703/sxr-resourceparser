import static main.FileRWD.*
import static main.Utils.fillLists
import static main.java.JavaUtils.createJavaTemplate
import static main.java.JavaUtilsStatic.*

/* ========== helper attributes   =============== */
Node        root
/* ========== script execution    =============== */
String path        = args.length > 0 ? args[0] : "./../../resources/peppol-bis-invoice-3/structure/syntax"
String outputDir   = args.length > 1 ? args[1] : "./sxr/model"
String rootFile    = "ubl-invoice.xml"
String packageName = 'sxr.model'
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


createXmlAttributeInterface (packageName + ".interfaces", outputDir + "/interfaces")
createXmlAttributesInterface(packageName + ".interfaces", outputDir + "/interfaces")
createXmlElementInterface   (packageName + ".interfaces", outputDir + "/interfaces")
createSxrObject             (packageName + ".entities", outputDir + "/entities")
createCodeLists             (packageName + ".codes", outputDir + "/codes", "$path/../codelist")
createPojoFile(root.Document[0] as Node,
               [className: 'Invoice', type:'Invoice.class', order: -1] as Map,
               packageName + ".entities.invoice", outputDir + "/entities/invoice",
               known)

// copy to submodule
def sourceDir = "$outputDir/../../sxr"
def targetDir = "$outputDir/../../../../sxr-javamodel/scr/sxr"

deleteDirectory(targetDir)
copyDirectory(sourceDir, targetDir)
println known
return 'success'

[
        Invoice: 1,
        DeliveryOrInvoicePeriod: 1,
        OrderAndSalesOrderReference: 1,
        PrecedingInvoiceReference: 1,
        InvoiceDocumentReference: 1,
        DespatchAdviceReference: 1,
        ReceiptAdviceReference: 1,
        TenderOrLotReference: 1,
        ContractReference: 1,
        AdditionalSupportingDocuments: 1,
        Attachment: 1,
        ExternalReference: 1,
        ProjectReference: 1,
        Seller: 1,
        Party: 2,
        PartyIdentification: 3,
        PartyName: 5,
        SellerPostalAddress: 1,
        AddressLine: 4,
        Country: 4,
        PartyVatTaxIdentifiers: 1,
        TaxScheme: 6,
        PartyLegalEntity: 3,
        SellerContact: 1,
        Buyer: 1,
        BuyerPostalAddress: 1,
        PartyVatIdentifier: 2,
        BuyerContact: 1,
        Payee: 1,
        SellerTaxRepresentativeParty: 1,
        SellerTaxRepresentativePostalAddress: 1,
        DeliveryInformation: 1,
        DeliveryLocation: 1,
        DeliverToAddress: 1,
        DeliverParty: 1,
        PaymentInstructions: 1,
        PaymentCardInformation: 1,
        CreditTransfer: 1,
        FinancialInstitutionBranch: 1,
        DirectDebit: 1,
        PayerFinancialAccount: 1,
        PaymentTerms: 1,
        DocumentLevelAllowancesAndCharges: 1,
        TaxCategory: 1,
        TaxTotal: 1,
        VatBreakdown: 1,
        VatCategory: 1,
        DocumentTotals: 1,
        InvoiceLine: 1,
        InvoiceLinePeriod: 1,
        OrderLineReference: 1,
        LineObjectIdentifier: 1,
        InvoiceLineAllowancesOrCharges: 1,
        ItemInformation: 1,
        BuyersItemIdentification: 1,
        SellersItemIdentification: 1,
        StandardItemIdentification: 1,
        OriginCountry: 1,
        CommodityClassification: 1,
        LineVatInformation: 1,
        ItemAttributes: 1,
        PriceDetails: 1,
        Allowance: 1
]
