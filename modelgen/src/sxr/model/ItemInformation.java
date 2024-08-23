package sxr.model;
import sxr.model.BuyersItemIdentification;
import sxr.model.SellersItemIdentification;
import sxr.model.StandardItemIdentification;
import sxr.model.OriginCountry;
import sxr.model.CommodityClassification;
import sxr.model.LineVatInformation;
import sxr.model.ItemAttributes;

public class ItemInformation {
	private String itemDescription;
	private String itemName;
	private BuyersItemIdentification buyersItemIdentification;
	private SellersItemIdentification sellersItemIdentification;
	private StandardItemIdentification standardItemIdentification;
	private OriginCountry originCountry;
	private CommodityClassification commodityClassification;
	private LineVatInformation lineVatInformation;
	private ItemAttributes itemAttributes;
}
