/**
 * 
 */
package net.poweregg.mitsubishi.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

/**
 * UMB Dto
 * 
 * @author dattdd
 *
 */
@Named(value = "UmbDto")
@ConversationScoped
public class Umb01Dto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Umb01Dto() {
		super();
	}
	
	/**
	 * 仮単価マスタデータ参照
	 */
	private String unitPriceDataRef;
	
	/**
	 * 単価マスタ参照
	 */
	private String priceDataRef;
	
	/**
	 * 二次店口銭率
	 */
	private BigDecimal secondStoreOpenRate;
	
	/**
	 * 二次店口銭額
	 */
	private BigDecimal secondStoreOpenAmount;
	
	/**
	 * 仕切単価(決定値)
	 */
	private BigDecimal partitionUnitPrice;
	
	/**
	 * 仮単価マスタ・価格マスタより参照Dto
	 */
	private PriceUnitRefDto unitPriceRefDto;
	
	/**
	 * 価格マスタより参照Dto
	 */
	private PriceRefDto priceRefDto;
	
	/**
	 * 
	 */
	private PriceCalParam priceCalParam;

	/**
	 * @return the unitPriceDataRef
	 */
	public String getUnitPriceDataRef() {
		return unitPriceDataRef;
	}

	/**
	 * @param unitPriceDataRef the unitPriceDataRef to set
	 */
	public void setUnitPriceDataRef(String unitPriceDataRef) {
		this.unitPriceDataRef = unitPriceDataRef;
	}

	/**
	 * @return the priceDataRef
	 */
	public String getPriceDataRef() {
		return priceDataRef;
	}

	/**
	 * @param priceDataRef the priceDataRef to set
	 */
	public void setPriceDataRef(String priceDataRef) {
		this.priceDataRef = priceDataRef;
	}

	/**
	 * @return the secondStoreOpenRate
	 */
	public BigDecimal getSecondStoreOpenRate() {
		return secondStoreOpenRate;
	}

	/**
	 * @param secondStoreOpenRate the secondStoreOpenRate to set
	 */
	public void setSecondStoreOpenRate(BigDecimal secondStoreOpenRate) {
		this.secondStoreOpenRate = secondStoreOpenRate;
	}

	/**
	 * @return the secondStoreOpenAmount
	 */
	public BigDecimal getSecondStoreOpenAmount() {
		return secondStoreOpenAmount;
	}

	/**
	 * @param secondStoreOpenAmount the secondStoreOpenAmount to set
	 */
	public void setSecondStoreOpenAmount(BigDecimal secondStoreOpenAmount) {
		this.secondStoreOpenAmount = secondStoreOpenAmount;
	}

	/**
	 * @return the partitionUnitPrice
	 */
	public BigDecimal getPartitionUnitPrice() {
		return partitionUnitPrice;
	}

	/**
	 * @param partitionUnitPrice the partitionUnitPrice to set
	 */
	public void setPartitionUnitPrice(BigDecimal partitionUnitPrice) {
		this.partitionUnitPrice = partitionUnitPrice;
	}

	/**
	 * @return the unitPriceRefDto
	 */
	public PriceUnitRefDto getUnitPriceRefDto() {
		return unitPriceRefDto;
	}

	/**
	 * @param unitPriceRefDto the unitPriceRefDto to set
	 */
	public void setUnitPriceRefDto(PriceUnitRefDto unitPriceRefDto) {
		this.unitPriceRefDto = unitPriceRefDto;
	}

	/**
	 * @return the priceRefDto
	 */
	public PriceRefDto getPriceRefDto() {
		return priceRefDto;
	}

	/**
	 * @param priceRefDto the priceRefDto to set
	 */
	public void setPriceRefDto(PriceRefDto priceRefDto) {
		this.priceRefDto = priceRefDto;
	}

	/**
	 * @return the priceCalParam
	 */
	public PriceCalParam getPriceCalParam() {
		return priceCalParam;
	}

	/**
	 * @param priceCalParam the priceCalParam to set
	 */
	public void setPriceCalParam(PriceCalParam priceCalParam) {
		this.priceCalParam = priceCalParam;
	}
}
