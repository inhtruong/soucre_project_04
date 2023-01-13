package net.poweregg.mitsubishi.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import net.poweregg.ui.param.WDBRefParam;

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
		this.priceUnitRefDto = new PriceUnitRefDto();
		this.priceRefDto = new PriceRefDto();
		this.priceCalParam = new PriceCalParam();
		this.unitPriceDataRef = new WDBRefParam();
		this.priceDataRef = new WDBRefParam();
	}
	
	private String id;
	
	/**
	 * 仮単価マスタデータ参照
	 */
	private WDBRefParam unitPriceDataRef;
	
	/**
	 * 単価マスタ参照
	 */
	private WDBRefParam priceDataRef;
	
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
	private PriceUnitRefDto priceUnitRefDto;
	
	/**
	 * 価格マスタより参照Dto
	 */
	private PriceRefDto priceRefDto;
	
	/**
	 * 
	 */
	private PriceCalParam priceCalParam;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the unitPriceDataRef
	 */
	public WDBRefParam getUnitPriceDataRef() {
		return unitPriceDataRef;
	}

	/**
	 * @param unitPriceDataRef the unitPriceDataRef to set
	 */
	public void setUnitPriceDataRef(WDBRefParam unitPriceDataRef) {
		this.unitPriceDataRef = unitPriceDataRef;
	}

	/**
	 * @return the priceDataRef
	 */
	public WDBRefParam getPriceDataRef() {
		return priceDataRef;
	}

	/**
	 * @param priceDataRef the priceDataRef to set
	 */
	public void setPriceDataRef(WDBRefParam priceDataRef) {
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
	 * @return the priceUnitRefDto
	 */
	public PriceUnitRefDto getPriceUnitRefDto() {
		return priceUnitRefDto;
	}

	/**
	 * @param priceUnitRefDto the priceUnitRefDto to set
	 */
	public void setPriceUnitRefDto(PriceUnitRefDto priceUnitRefDto) {
		this.priceUnitRefDto = priceUnitRefDto;
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
