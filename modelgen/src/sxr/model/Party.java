package sxr.model;
import sxr.model.PartyIdentification;
import sxr.model.PartyName;
import sxr.model.BuyerPostalAddress;
import sxr.model.PartyVatIdentifier;
import sxr.model.PartyLegalEntity;
import sxr.model.BuyerContact;

public class Party {
	private String buyerElectronicAddress;
	private PartyIdentification partyIdentification;
	private PartyName partyName;
	private BuyerPostalAddress buyerPostalAddress;
	private PartyVatIdentifier partyVatIdentifier;
	private PartyLegalEntity partyLegalEntity;
	private BuyerContact buyerContact;
}
