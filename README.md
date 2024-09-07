# Modelgeneration for SXR
## Current Approach
Protobuf java classes seemed difficult to work with, so instead im gonna parse the 
Pojo classes myself and using annotations to create structure for further parsing.

E.g.
```java
package sxr.model.entities;
import sxr.model.interfaces.*;
import sxr.model.codes.*;
import java.util.List;

@XmlElement(term = "null", btRef = "null", order = -1, min = 1, max = 1 )
public class Invoice {
    /* =========== Basic Properties   =========== */
    @XmlElement(term = "cbc:CustomizationID", btRef = "BT-24", order = 0, min = 1, max = 1 )
    public String specificationIdentifier;
    @XmlElement(term = "cbc:ProfileID", btRef = "BT-23", order = 1, min = 1, max = 1 )
    public String businessProcessType;
    @XmlElement(term = "cbc:ID", btRef = "BT-1", order = 2, min = 1, max = 1 )
    public String invoiceNumber;
    @XmlElement(term = "cbc:IssueDate", btRef = "BT-2", order = 3, min = 1, max = 1 )
    public java.util.Date invoiceIssueDate;
    @XmlElement(term = "cbc:DueDate", btRef = "BT-9", order = 4, min = 0, max = 1 )
    public java.util.Date paymentDueDate;
    @XmlElement(term = "cbc:InvoiceTypeCode", btRef = "BT-3", order = 5, min = 1, max = 1 )
    public Uncl1001Inv invoiceTypeCode;
    @XmlElement(term = "cbc:Note", btRef = "BT-22", order = 6, min = 0, max = 1 )
    public String invoiceNote;
    @XmlElement(term = "cbc:TaxPointDate", btRef = "BT-7", order = 7, min = 0, max = 1 )
    public java.util.Date valueAddedTaxPointDate;
    @XmlElement(term = "cbc:DocumentCurrencyCode", btRef = "BT-5", order = 8, min = 1, max = 1 )
    public Iso4217 invoiceCurrencyCode;
    @XmlElement(term = "cbc:TaxCurrencyCode", btRef = "BT-6", order = 9, min = 0, max = 1 )
    public Iso4217 vatAccountingCurrencyCode;
    @XmlElement(term = "cbc:AccountingCost", btRef = "BT-19", order = 10, min = 0, max = 1 )
    public String buyerAccountingReference;
    @XmlElement(term = "cbc:BuyerReference", btRef = "BT-10", order = 11, min = 0, max = 1 )
    public String buyerReference;

    /* =========== Complex Properties =========== */
    @XmlElement(term = "cac:InvoicePeriod", btRef = "BG-14", order = 12, min = 0, max = 1 )
    public DeliveryOrInvoicePeriod deliveryOrInvoicePeriod;
    @XmlElement(term = "cac:OrderReference", btRef = "null", order = 13, min = 0, max = 1 )
    public OrderAndSalesOrderReference orderAndSalesOrderReference;
    @XmlElement(term = "cac:BillingReference", btRef = "BG-3", order = 14, min = 0, max = -1 )
    public List<PrecedingInvoiceReference> precedingInvoiceReference;
    @XmlElement(term = "cac:DespatchDocumentReference", btRef = "null", order = 15, min = 0, max = 1 )
    public DespatchAdviceReference despatchAdviceReference;
    @XmlElement(term = "cac:ReceiptDocumentReference", btRef = "null", order = 16, min = 0, max = 1 )
    public ReceiptAdviceReference receiptAdviceReference;
    @XmlElement(term = "cac:OriginatorDocumentReference", btRef = "null", order = 17, min = 0, max = 1 )
    public TenderOrLotReference tenderOrLotReference;
    @XmlElement(term = "cac:ContractDocumentReference", btRef = "null", order = 18, min = 0, max = 1 )
    public ContractReference contractReference;
    @XmlElement(term = "cac:AdditionalDocumentReference", btRef = "BG-24", order = 19, min = 0, max = -1 )
    public List<AdditionalSupportingDocuments> additionalSupportingDocuments;
    @XmlElement(term = "cac:ProjectReference", btRef = "null", order = 20, min = 0, max = 1 )
    public ProjectReference projectReference;
    @XmlElement(term = "cac:AccountingSupplierParty", btRef = "BG-4", order = 21, min = 1, max = 1 )
    public Seller seller;
    @XmlElement(term = "cac:AccountingCustomerParty", btRef = "BG-7", order = 22, min = 1, max = 1 )
    public Buyer buyer;
    @XmlElement(term = "cac:PayeeParty", btRef = "BG-10", order = 23, min = 0, max = 1 )
    public Payee payee;
    @XmlElement(term = "cac:TaxRepresentativeParty", btRef = "BG-11", order = 24, min = 0, max = 1 )
    public SellerTaxRepresentativeParty sellerTaxRepresentativeParty;
    @XmlElement(term = "cac:Delivery", btRef = "BG-13", order = 25, min = 0, max = 1 )
    public DeliveryInformation deliveryInformation;
    @XmlElement(term = "cac:PaymentMeans", btRef = "BG-16", order = 26, min = 0, max = -1 )
    public List<PaymentInstructions> paymentInstructions;
    @XmlElement(term = "cac:PaymentTerms", btRef = "null", order = 27, min = 0, max = 1 )
    public PaymentTerms paymentTerms;
    @XmlElement(term = "cac:AllowanceCharge", btRef = "BG-20, BG-21", order = 28, min = 0, max = -1 )
    public List<DocumentLevelAllowancesAndCharges> documentLevelAllowancesAndCharges;
    @XmlElement(term = "cac:TaxTotal", btRef = "null", order = 29, min = 1, max = 2 )
    public TaxTotal taxTotal;
    @XmlElement(term = "cac:LegalMonetaryTotal", btRef = "BG-22", order = 30, min = 1, max = 1 )
    public DocumentTotals documentTotals;
    @XmlElement(term = "cac:InvoiceLine", btRef = "BG-25", order = 31, min = 1, max = -1 )
    public List<InvoiceLine> invoiceLine;
}


```
## Building
The classes can be compiled via _GeneratePojo.groovy_.

Alternatively they are incorporated into a gradle build step
```bash
gradle generateSources
```
The following allows packaging to a jar
```bash
gradle clean build
```

---
### Old Approach
The general idea is to parse Peppol Invoice Syntax model to 
generate Protobuf Types. From there it should be possible to generate
Frontend aswell as Backend Types.

#### Install Protobuf compiler
https://github.com/protocolbuffers/protobuf/releases

For Windows: Download + add to path

Verify with 
```bash 
protoc -h
```

## Usage
1. Generate proto-model using the **modelgen** Modul(Groovy).
2. Generate language specific code.
```bash
 protoc --kotlin_out=modelgen/kotlin_out --java_out=modelgen/kotlin_out --proto_path=modelgen/proto-model DELIVERY_INFORMATION.proto
```