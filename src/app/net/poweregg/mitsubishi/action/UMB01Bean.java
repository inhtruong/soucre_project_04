package net.poweregg.mitsubishi.action;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import net.poweregg.annotations.PEIntercepter;
import net.poweregg.common.ClassificationService;
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.webdb.utils.CSVUtils;

@Named("UMB01Bean")
@ConversationScoped
@PEIntercepter
public class UMB01Bean implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @EJB
  private ClassificationService classificationService;
  
  private Integer returnCode;
  
  private String csvFilePath;
  
  public void initUMB0101e() {
    try {
      importCSV();
      setReturnCode(Integer.valueOf(0));
    } catch (Exception e) {
      setReturnCode(Integer.valueOf(1));
    } 
  }
  
  private void importCSV() throws Exception {
    File importFile = new File(this.csvFilePath);
    List<String[]> contentFile = CSVUtils.readAllToListString(importFile, ",", "MS932", true);
    int temp = 0;
  }
  
  private Umb01Dto umb01Dto;

  public void calculateValue() {
		BigDecimal retailPrice = umb01Dto.getPriceRefDto().getRetailPrice();
		BigDecimal unitPriceSmallParcel = umb01Dto.getPriceRefDto().getUnitPriceSmallParcel();
		BigDecimal unitPriceForeheadColor = umb01Dto.getPriceRefDto().getUnitPriceForeheadColor();
		BigDecimal primaryStoreOpenRate = umb01Dto.getPriceRefDto().getPrimaryStoreOpenRate();
		BigDecimal primaryStoreCommissionAmount = umb01Dto.getPriceRefDto().getPrimaryStoreCommissionAmount();
		BigDecimal secondStoreOpenRate = umb01Dto.getSecondStoreOpenRate();
		BigDecimal secondStoreOpenAmount = umb01Dto.getSecondStoreOpenAmount();
		BigDecimal lotQuantity = umb01Dto.getPriceRefDto().getLotQuantity();
		BigDecimal calValue = new BigDecimal("0");
		BigDecimal valueLotSmall = new BigDecimal("100");
		BigDecimal valueLotLarge = new BigDecimal("100");

		// pattern 1
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
					.subtract(secondStoreOpenRate.multiply(retailPrice));
		}
	  
		// pattern 2
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			calValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
		}

		// pattern 3
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceSmallParcel)
						.subtract(primaryStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)))
						.subtract(secondStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)));
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 4
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceSmallParcel).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				calValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			}
		}

		// pattern 5
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor)
						.subtract(primaryStoreOpenRate.multiply(retailPrice.add(unitPriceForeheadColor)))
						.subtract(secondStoreOpenRate.multiply(retailPrice.add(unitPriceForeheadColor)));
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 6
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				calValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			}
		}

		// pattern 7
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) >= 0 && valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)
						.subtract(primaryStoreOpenRate
								.multiply(retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)))
						.subtract(secondStoreOpenRate
								.multiply(retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)));
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 8
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor).add(unitPriceSmallParcel)
						.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			} else if (valueLotSmall.compareTo(lotQuantity) >= 0 && valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

  }
  
  public String getCsvFilePath() {
    return this.csvFilePath;
  }
  
  public void setCsvFilePath(String csvFilePath) {
    this.csvFilePath = csvFilePath;
  }
  
  public Integer getReturnCode() {
    return this.returnCode;
  }
  
  public void setReturnCode(Integer returnCode) {
    this.returnCode = returnCode;
  }
}
