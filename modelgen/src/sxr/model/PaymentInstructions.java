package sxr.model;
import sxr.model.PaymentCardInformation;
import sxr.model.CreditTransfer;
import sxr.model.DirectDebit;

public class PaymentInstructions {
	private String paymentMeansTypeCode;
	private String remittanceInformation;
	private PaymentCardInformation paymentCardInformation;
	private CreditTransfer creditTransfer;
	private DirectDebit directDebit;
}
