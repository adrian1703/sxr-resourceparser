# Modelgeneration for SXR
## Current Approach
Protobuf java classes seemed difficult to work with, so instead im gonna parse the 
Pojo classes myself and using annoations to create structure for further parsing.

E.g.
```java
package sxr.model.entities;
import sxr.model.interfaces.*;

@XmlElement(term = "null", btRef = "null", order = -1, mandatory = true )
public class Invoice {
	/* =========== Basic Properties   =========== */
	@XmlElement(term = "cbc:CustomizationID", btRef = "BT-24", order = 0, mandatory = true )
	private String specificationIdentifier;
	@XmlElement(term = "cbc:ProfileID", btRef = "BT-23", order = 1, mandatory = true )
	private String businessProcessType;
	@XmlElement(term = "cbc:ID", btRef = "BT-1", order = 2, mandatory = true )
	private String invoiceNumber;
	@XmlElement(term = "cbc:IssueDate", btRef = "BT-2", order = 3, mandatory = true )
	private String invoiceIssueDate;
	@XmlElement(term = "cbc:DueDate", btRef = "BT-9", order = 4, mandatory = false )
	private String paymentDueDate;
	@XmlElement(term = "cbc:InvoiceTypeCode", btRef = "BT-3", order = 5, mandatory = true )
	private String invoiceTypeCode;
	@XmlElement(term = "cbc:Note", btRef = "BT-22", order = 6, mandatory = false )
	private String invoiceNote;
	@XmlElement(term = "cbc:TaxPointDate", btRef = "BT-7", order = 7, mandatory = false )
	private String valueAddedTaxPointDate;
	@XmlElement(term = "cbc:DocumentCurrencyCode", btRef = "BT-5", order = 8, mandatory = true )
	private String invoiceCurrencyCode;
	@XmlElement(term = "cbc:TaxCurrencyCode", btRef = "BT-6", order = 9, mandatory = false )
	private String vatAccountingCurrencyCode;
	@XmlElement(term = "cbc:AccountingCost", btRef = "BT-19", order = 10, mandatory = false )
	private String buyerAccountingReference;
	@XmlElement(term = "cbc:BuyerReference", btRef = "BT-10", order = 11, mandatory = false )
	private String buyerReference;

	/* =========== Complex Properties =========== */
	@XmlElement(term = "cac:InvoicePeriod", btRef = "BG-14", order = 12, mandatory = false )
	private DeliveryOrInvoicePeriod deliveryOrInvoicePeriod;
	@XmlElement(term = "cac:OrderReference", btRef = "null", order = 13, mandatory = false )
	private OrderAndSalesOrderReference orderAndSalesOrderReference;
	@XmlElement(term = "cac:BillingReference", btRef = "BG-3", order = 14, mandatory = false )
	private PrecedingInvoiceReference precedingInvoiceReference;
	@XmlElement(term = "cac:DespatchDocumentReference", btRef = "null", order = 15, mandatory = false )
	private DespatchAdviceReference despatchAdviceReference;
	@XmlElement(term = "cac:ReceiptDocumentReference", btRef = "null", order = 16, mandatory = false )
	private ReceiptAdviceReference receiptAdviceReference;
	@XmlElement(term = "cac:OriginatorDocumentReference", btRef = "null", order = 17, mandatory = false )
	private TenderOrLotReference tenderOrLotReference;
	@XmlElement(term = "cac:ContractDocumentReference", btRef = "null", order = 18, mandatory = false )
	private ContractReference contractReference;
	@XmlElement(term = "cac:AdditionalDocumentReference", btRef = "BG-24", order = 19, mandatory = false )
	private AdditionalSupportingDocuments additionalSupportingDocuments;
	@XmlElement(term = "cac:ProjectReference", btRef = "null", order = 20, mandatory = false )
	private ProjectReference projectReference;
	@XmlElement(term = "cac:AccountingSupplierParty", btRef = "BG-4", order = 21, mandatory = true )
	private Seller seller;
	@XmlElement(term = "cac:AccountingCustomerParty", btRef = "BG-7", order = 22, mandatory = true )
	private Buyer buyer;
	@XmlElement(term = "cac:PayeeParty", btRef = "BG-10", order = 23, mandatory = false )
	private Payee payee;
	@XmlElement(term = "cac:TaxRepresentativeParty", btRef = "BG-11", order = 24, mandatory = false )
	private SellerTaxRepresentativeParty sellerTaxRepresentativeParty;
	@XmlElement(term = "cac:Delivery", btRef = "BG-13", order = 25, mandatory = false )
	private DeliveryInformation deliveryInformation;
	@XmlElement(term = "cac:PaymentMeans", btRef = "BG-16", order = 26, mandatory = false )
	private PaymentInstructions paymentInstructions;
	@XmlElement(term = "cac:PaymentTerms", btRef = "null", order = 27, mandatory = false )
	private PaymentTerms paymentTerms;
	@XmlElement(term = "cac:AllowanceCharge", btRef = "BG-20, BG-21", order = 28, mandatory = false )
	private DocumentLevelAllowancesAndCharges documentLevelAllowancesAndCharges;
	@XmlElement(term = "cac:TaxTotal", btRef = "null", order = 29, mandatory = true )
	private TaxTotal taxTotal;
	@XmlElement(term = "cac:LegalMonetaryTotal", btRef = "BG-22", order = 30, mandatory = true )
	private DocumentTotals documentTotals;
	@XmlElement(term = "cac:InvoiceLine", btRef = "BG-25", order = 31, mandatory = true )
	private InvoiceLine invoiceLine;

}

```
## Building
The classes can be compiled via _GeneratePojo.groovy_.

Alternativly they are incorporated into a gradle build step
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