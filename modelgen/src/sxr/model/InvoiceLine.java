package sxr.model;
import sxr.model.InvoiceLinePeriod;
import sxr.model.OrderLineReference;
import sxr.model.LineObjectIdentifier;
import sxr.model.InvoiceLineAllowancesOrCharges;
import sxr.model.ItemInformation;
import sxr.model.PriceDetails;

public class InvoiceLine {
	private String invoiceLineIdentifier;
	private String invoiceLineNote;
	private String invoicedQuantity;
	private String invoiceLineNetAmount;
	private String invoiceLineBuyerAccountingReference;
	private InvoiceLinePeriod invoiceLinePeriod;
	private OrderLineReference orderLineReference;
	private LineObjectIdentifier lineObjectIdentifier;
	private InvoiceLineAllowancesOrCharges invoiceLineAllowancesOrCharges;
	private ItemInformation itemInformation;
	private PriceDetails priceDetails;
}
