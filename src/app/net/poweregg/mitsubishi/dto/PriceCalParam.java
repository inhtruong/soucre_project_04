/**
 * 
 */
package net.poweregg.mitsubishi.dto;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

/**
 * Price Calculator Param
 * 
 * @author dattdd
 *
 */
@Named(value = "PriceCalParam")
@ConversationScoped
public class PriceCalParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public PriceCalParam() {
		super();
	}
	
	private String pattern;
	
	/**
	 * 末端価格1(割増無)
	 */
	private String noPreRetailPrice1;
	
	/**
	 * 末端価格2(割増無)
	 */
	private String noPreRetailPrice2;
	
	/**
	 * 末端価格1(配送)
	 */
	private String deliRetailPrice1;
	
	/**
	 * 末端価格2(配送)
	 */
	private String deliRetailPrice2;
	
	/**
	 * 末端価格1(299kg)
	 */
	private String largeRetailPrice1;
	
	/**
	 * 末端価格2(299kg)
	 */
	private String largeRetailPrice2;
	
	/**
	 * 末端価格1(100kg)
	 */
	private String smallRetailPrice1;
	
	/**
	 * 末端価格2(100kg)
	 */
	private String smallRetailPrice2;
	
	/**
	 * 小口配送単価1(配送)
	 */
	private String deliUnitPriceParcel1;
	
	/**
	 * 小口配送単価2(配送)
	 */
	private String deliUnitPriceParcel2;
	
	/**
	 * 小口配送単価1(100kg)
	 */
	private String smallUnitPriceParcel1;
	
	/**
	 * 小口配送単価2(100kg)
	 */
	private String smallUnitPriceParcel2;
	
	/**
	 * 小口着色単価1(299kg)
	 */
	private String largeUnitPriceForehead1;
	
	/**
	 * 小口着色単価2(299kg)
	 */
	private String largeUnitPriceForehead2;
	
	/**
	 * 小口着色単価1(100kg)
	 */
	private String smallUnitPriceForehead1;
	
	/**
	 * 小口着色単価2(100kg)
	 */
	private String smallUnitPriceForehead2;
	
	/**
	 * 末端価格合計1(割増無)
	 */
	private String noPreTotalRetailPrice1;
	
	/**
	 * 末端価格合計2(割増無)
	 */
	private String noPreTotalRetailPrice2;
	
	/**
	 * 末端価格合計1(配送)
	 */
	private String deliTotalRetailPrice1;
	
	/**
	 * 末端価格合計1(配送)
	 */
	private String deliTotalRetailPrice2;
	
	/**
	 * 末端価格合計1(299kg)
	 */
	private String largeTotalRetailPrice1;
	
	/**
	 * 末端価格合計2(299kg)
	 */
	private String largeTotalRetailPrice2;
	
	/**
	 * 末端価格合計1(100kg)
	 */
	private String smallTotalRetailPrice1;
	
	/**
	 * 末端価格合計2(100kg)
	 */
	private String smallTotalRetailPrice2;
	
	/**
	 * 一次店口銭(金額)(割増無)
	 */
	private String noPrePrimaryOpenAmount;
	
	/**
	 * 一次店口銭(金額)(配送)
	 */
	private String deliPrimaryOpenAmount;
	
	/**
	 * 一次店口銭(金額)(299kg)
	 */
	private String largePrimaryOpenAmount;
	
	/**
	 * 一次店口銭(金額)(100kg)
	 */
	private String smallPrimaryOpenAmount;
	
	/**
	 * 二次店口銭(金額)(割増無)
	 */
	private String noPreSecondaryOpenAmount;
	
	/**
	 * 二次店口銭(金額)(配送)
	 */
	private String deliSecondaryOpenAmount;
	
	/**
	 * 二次店口銭(金額)(299kg)
	 */
	private String largeSecondaryOpenAmount;
	
	/**
	 * 二次店口銭(金額)(100kg)
	 */
	private String smallSecondaryOpenAmount;
	
	/**
	 * 一次店口銭率(割増無)
	 */
	private String noPrePrimaryOpenRate;
	
	/**
	 * 一次店口銭率(配送)
	 */
	private String deliPrimaryOpenRate;
	
	/**
	 * 一次店口銭率(299kg)
	 */
	private String largePrimaryOpenRate;
	
	/**
	 * 一次店口銭率(100kg)
	 */
	private String smallPrimaryOpenRate;
	
	/**
	 * 二次店口銭率(割増無)
	 */
	private String noPreSecondaryOpenRate;
	
	/**
	 * 二次店口銭率(配送)
	 */
	private String deliSecondaryOpenRate;
	
	/**
	 * 二次店口銭率(299kg)
	 */
	private String largeSecondaryOpenRate;
	
	/**
	 * 二次店口銭率(100kg)
	 */
	private String smallSecondaryOpenRate;
	
	/**
	 * 一次店口銭率(割増無)
	 */
	private String noPreTotalOpenRate;
	
	/**
	 * 口銭合計率(配送)
	 */
	private String deliTotalOpenRate;
	
	/**
	 * 口銭合計率(299kg)
	 */
	private String largeTotalOpenRate;
	
	/**
	 *口銭合計率(100kg)
	 */
	private String smallTotalOpenRate;
	
	/**
	 * 口銭合計(金額)(割増無)
	 */
	private String noPreTotalOpenAmount;
	
	/**
	 * 口銭合計(金額)(配送)
	 */
	private String deliTotalOpenAmount;
	
	/**
	 * 口銭合計(金額)(299kg)
	 */
	private String largeTotalOpenAmount;
	
	/**
	 * 口銭合計(金額)(100kg)
	 */
	private String smallTotalOpenAmount;
	
	/**
	 * 一次店口銭金額(割増無)
	 */
	private String noPrePrimaryCommisAmount;
	
	/**
	 * 一次店口銭金額(配送)
	 */
	private String deliPrimaryCommisAmount;
	
	/**
	 * 一次店口銭金額(299kg)
	 */
	private String largePrimaryCommisAmount;
	
	/**
	 * 一次店口銭金額(100kg)
	 */
	private String smallPrimaryCommisAmount;
	
	/**
	 * 二次店口銭率
	 */
	private String secondStoreOpenRate;
	
	/**
	 * 二次店口銭額
	 */
	private String secondStoreOpenAmount;
	
	/**
	 * 仕切単価1(計算値)(割増無)
	 */
	private String noPrePartitionUnitPrice1;
	
	/**
	 * 仕切単価2(計算値)(割増無)
	 */
	private String noPrePartitionUnitPrice2;
	
	/**
	 * 仕切単価1(計算値)(小口)
	 */
	private String deliPartitionUnitPrice1;
	
	/**
	 * 仕切単価2(計算値)(小口)
	 */
	private String deliPartitionUnitPrice2;
	
	/**
	 * 仕切単価1(計算値)(299kg)
	 */
	private String largePartitionUnitPrice1;
	
	/**
	 * 仕切単価2(計算値)(299kg)
	 */
	private String largePartitionUnitPrice2;
	
	/**
	 * 仕切単価2(計算値)(100kg)
	 */
	private String smallPartitionUnitPrice1;
	
	/**
	 * 仕切単価2(計算値)(100kg)
	 */
	private String smallPartitionUnitPrice2;
	
	/**
	 * 小口配送単価(計算値)
	 */
	private String calUnitPriceParcel;
	
	/**
	 * 小口配送単価(計算値)
	 */
	private String calUnitPriceForehead;
	
	

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the noPreRetailPrice1
	 */
	public String getNoPreRetailPrice1() {
		return noPreRetailPrice1;
	}

	/**
	 * @param noPreRetailPrice1 the noPreRetailPrice1 to set
	 */
	public void setNoPreRetailPrice1(String noPreRetailPrice1) {
		this.noPreRetailPrice1 = noPreRetailPrice1;
	}

	/**
	 * @return the noPreRetailPrice2
	 */
	public String getNoPreRetailPrice2() {
		return noPreRetailPrice2;
	}

	/**
	 * @param noPreRetailPrice2 the noPreRetailPrice2 to set
	 */
	public void setNoPreRetailPrice2(String noPreRetailPrice2) {
		this.noPreRetailPrice2 = noPreRetailPrice2;
	}

	/**
	 * @return the deliRetailPrice1
	 */
	public String getDeliRetailPrice1() {
		return deliRetailPrice1;
	}

	/**
	 * @param deliRetailPrice1 the deliRetailPrice1 to set
	 */
	public void setDeliRetailPrice1(String deliRetailPrice1) {
		this.deliRetailPrice1 = deliRetailPrice1;
	}

	/**
	 * @return the deliRetailPrice2
	 */
	public String getDeliRetailPrice2() {
		return deliRetailPrice2;
	}

	/**
	 * @param deliRetailPrice2 the deliRetailPrice2 to set
	 */
	public void setDeliRetailPrice2(String deliRetailPrice2) {
		this.deliRetailPrice2 = deliRetailPrice2;
	}

	/**
	 * @return the largeRetailPrice1
	 */
	public String getLargeRetailPrice1() {
		return largeRetailPrice1;
	}

	/**
	 * @param largeRetailPrice1 the largeRetailPrice1 to set
	 */
	public void setLargeRetailPrice1(String largeRetailPrice1) {
		this.largeRetailPrice1 = largeRetailPrice1;
	}

	/**
	 * @return the largeRetailPrice2
	 */
	public String getLargeRetailPrice2() {
		return largeRetailPrice2;
	}

	/**
	 * @param largeRetailPrice2 the largeRetailPrice2 to set
	 */
	public void setLargeRetailPrice2(String largeRetailPrice2) {
		this.largeRetailPrice2 = largeRetailPrice2;
	}

	/**
	 * @return the smallRetailPrice1
	 */
	public String getSmallRetailPrice1() {
		return smallRetailPrice1;
	}

	/**
	 * @param smallRetailPrice1 the smallRetailPrice1 to set
	 */
	public void setSmallRetailPrice1(String smallRetailPrice1) {
		this.smallRetailPrice1 = smallRetailPrice1;
	}

	/**
	 * @return the smallRetailPrice2
	 */
	public String getSmallRetailPrice2() {
		return smallRetailPrice2;
	}

	/**
	 * @param smallRetailPrice2 the smallRetailPrice2 to set
	 */
	public void setSmallRetailPrice2(String smallRetailPrice2) {
		this.smallRetailPrice2 = smallRetailPrice2;
	}

	/**
	 * @return the deliUnitPriceParcel1
	 */
	public String getDeliUnitPriceParcel1() {
		return deliUnitPriceParcel1;
	}

	/**
	 * @param deliUnitPriceParcel1 the deliUnitPriceParcel1 to set
	 */
	public void setDeliUnitPriceParcel1(String deliUnitPriceParcel1) {
		this.deliUnitPriceParcel1 = deliUnitPriceParcel1;
	}

	/**
	 * @return the deliUnitPriceParcel2
	 */
	public String getDeliUnitPriceParcel2() {
		return deliUnitPriceParcel2;
	}

	/**
	 * @param deliUnitPriceParcel2 the deliUnitPriceParcel2 to set
	 */
	public void setDeliUnitPriceParcel2(String deliUnitPriceParcel2) {
		this.deliUnitPriceParcel2 = deliUnitPriceParcel2;
	}

	/**
	 * @return the smallUnitPriceParcel1
	 */
	public String getSmallUnitPriceParcel1() {
		return smallUnitPriceParcel1;
	}

	/**
	 * @param smallUnitPriceParcel1 the smallUnitPriceParcel1 to set
	 */
	public void setSmallUnitPriceParcel1(String smallUnitPriceParcel1) {
		this.smallUnitPriceParcel1 = smallUnitPriceParcel1;
	}

	/**
	 * @return the smallUnitPriceParcel2
	 */
	public String getSmallUnitPriceParcel2() {
		return smallUnitPriceParcel2;
	}

	/**
	 * @param smallUnitPriceParcel2 the smallUnitPriceParcel2 to set
	 */
	public void setSmallUnitPriceParcel2(String smallUnitPriceParcel2) {
		this.smallUnitPriceParcel2 = smallUnitPriceParcel2;
	}

	/**
	 * @return the largeUnitPriceForehead1
	 */
	public String getLargeUnitPriceForehead1() {
		return largeUnitPriceForehead1;
	}

	/**
	 * @param largeUnitPriceForehead1 the largeUnitPriceForehead1 to set
	 */
	public void setLargeUnitPriceForehead1(String largeUnitPriceForehead1) {
		this.largeUnitPriceForehead1 = largeUnitPriceForehead1;
	}

	/**
	 * @return the largeUnitPriceForehead2
	 */
	public String getLargeUnitPriceForehead2() {
		return largeUnitPriceForehead2;
	}

	/**
	 * @param largeUnitPriceForehead2 the largeUnitPriceForehead2 to set
	 */
	public void setLargeUnitPriceForehead2(String largeUnitPriceForehead2) {
		this.largeUnitPriceForehead2 = largeUnitPriceForehead2;
	}

	/**
	 * @return the smallUnitPriceForehead1
	 */
	public String getSmallUnitPriceForehead1() {
		return smallUnitPriceForehead1;
	}

	/**
	 * @param smallUnitPriceForehead1 the smallUnitPriceForehead1 to set
	 */
	public void setSmallUnitPriceForehead1(String smallUnitPriceForehead1) {
		this.smallUnitPriceForehead1 = smallUnitPriceForehead1;
	}

	/**
	 * @return the smallUnitPriceForehead2
	 */
	public String getSmallUnitPriceForehead2() {
		return smallUnitPriceForehead2;
	}

	/**
	 * @param smallUnitPriceForehead2 the smallUnitPriceForehead2 to set
	 */
	public void setSmallUnitPriceForehead2(String smallUnitPriceForehead2) {
		this.smallUnitPriceForehead2 = smallUnitPriceForehead2;
	}

	/**
	 * @return the noPreTotalRetailPrice1
	 */
	public String getNoPreTotalRetailPrice1() {
		return noPreTotalRetailPrice1;
	}

	/**
	 * @param noPreTotalRetailPrice1 the noPreTotalRetailPrice1 to set
	 */
	public void setNoPreTotalRetailPrice1(String noPreTotalRetailPrice1) {
		this.noPreTotalRetailPrice1 = noPreTotalRetailPrice1;
	}

	/**
	 * @return the noPreTotalRetailPrice2
	 */
	public String getNoPreTotalRetailPrice2() {
		return noPreTotalRetailPrice2;
	}

	/**
	 * @param noPreTotalRetailPrice2 the noPreTotalRetailPrice2 to set
	 */
	public void setNoPreTotalRetailPrice2(String noPreTotalRetailPrice2) {
		this.noPreTotalRetailPrice2 = noPreTotalRetailPrice2;
	}

	/**
	 * @return the deliTotalRetailPrice1
	 */
	public String getDeliTotalRetailPrice1() {
		return deliTotalRetailPrice1;
	}

	/**
	 * @param deliTotalRetailPrice1 the deliTotalRetailPrice1 to set
	 */
	public void setDeliTotalRetailPrice1(String deliTotalRetailPrice1) {
		this.deliTotalRetailPrice1 = deliTotalRetailPrice1;
	}

	/**
	 * @return the deliTotalRetailPrice2
	 */
	public String getDeliTotalRetailPrice2() {
		return deliTotalRetailPrice2;
	}

	/**
	 * @param deliTotalRetailPrice2 the deliTotalRetailPrice2 to set
	 */
	public void setDeliTotalRetailPrice2(String deliTotalRetailPrice2) {
		this.deliTotalRetailPrice2 = deliTotalRetailPrice2;
	}

	/**
	 * @return the largeTotalRetailPrice1
	 */
	public String getLargeTotalRetailPrice1() {
		return largeTotalRetailPrice1;
	}

	/**
	 * @param largeTotalRetailPrice1 the largeTotalRetailPrice1 to set
	 */
	public void setLargeTotalRetailPrice1(String largeTotalRetailPrice1) {
		this.largeTotalRetailPrice1 = largeTotalRetailPrice1;
	}

	/**
	 * @return the largeTotalRetailPrice2
	 */
	public String getLargeTotalRetailPrice2() {
		return largeTotalRetailPrice2;
	}

	/**
	 * @param largeTotalRetailPrice2 the largeTotalRetailPrice2 to set
	 */
	public void setLargeTotalRetailPrice2(String largeTotalRetailPrice2) {
		this.largeTotalRetailPrice2 = largeTotalRetailPrice2;
	}

	/**
	 * @return the smallTotalRetailPrice1
	 */
	public String getSmallTotalRetailPrice1() {
		return smallTotalRetailPrice1;
	}

	/**
	 * @param smallTotalRetailPrice1 the smallTotalRetailPrice1 to set
	 */
	public void setSmallTotalRetailPrice1(String smallTotalRetailPrice1) {
		this.smallTotalRetailPrice1 = smallTotalRetailPrice1;
	}

	/**
	 * @return the smallTotalRetailPrice2
	 */
	public String getSmallTotalRetailPrice2() {
		return smallTotalRetailPrice2;
	}

	/**
	 * @param smallTotalRetailPrice2 the smallTotalRetailPrice2 to set
	 */
	public void setSmallTotalRetailPrice2(String smallTotalRetailPrice2) {
		this.smallTotalRetailPrice2 = smallTotalRetailPrice2;
	}

	/**
	 * @return the noPrePrimaryOpenAmount
	 */
	public String getNoPrePrimaryOpenAmount() {
		return noPrePrimaryOpenAmount;
	}

	/**
	 * @param noPrePrimaryOpenAmount the noPrePrimaryOpenAmount to set
	 */
	public void setNoPrePrimaryOpenAmount(String noPrePrimaryOpenAmount) {
		this.noPrePrimaryOpenAmount = noPrePrimaryOpenAmount;
	}

	/**
	 * @return the deliPrimaryOpenAmount
	 */
	public String getDeliPrimaryOpenAmount() {
		return deliPrimaryOpenAmount;
	}

	/**
	 * @param deliPrimaryOpenAmount the deliPrimaryOpenAmount to set
	 */
	public void setDeliPrimaryOpenAmount(String deliPrimaryOpenAmount) {
		this.deliPrimaryOpenAmount = deliPrimaryOpenAmount;
	}

	/**
	 * @return the largePrimaryOpenAmount
	 */
	public String getLargePrimaryOpenAmount() {
		return largePrimaryOpenAmount;
	}

	/**
	 * @param largePrimaryOpenAmount the largePrimaryOpenAmount to set
	 */
	public void setLargePrimaryOpenAmount(String largePrimaryOpenAmount) {
		this.largePrimaryOpenAmount = largePrimaryOpenAmount;
	}

	/**
	 * @return the smallPrimaryOpenAmount
	 */
	public String getSmallPrimaryOpenAmount() {
		return smallPrimaryOpenAmount;
	}

	/**
	 * @param smallPrimaryOpenAmount the smallPrimaryOpenAmount to set
	 */
	public void setSmallPrimaryOpenAmount(String smallPrimaryOpenAmount) {
		this.smallPrimaryOpenAmount = smallPrimaryOpenAmount;
	}

	/**
	 * @return the noPrePrimaryOpenRate
	 */
	public String getNoPrePrimaryOpenRate() {
		return noPrePrimaryOpenRate;
	}

	/**
	 * @param noPrePrimaryOpenRate the noPrePrimaryOpenRate to set
	 */
	public void setNoPrePrimaryOpenRate(String noPrePrimaryOpenRate) {
		this.noPrePrimaryOpenRate = noPrePrimaryOpenRate;
	}

	/**
	 * @return the deliPrimaryOpenRate
	 */
	public String getDeliPrimaryOpenRate() {
		return deliPrimaryOpenRate;
	}

	/**
	 * @param deliPrimaryOpenRate the deliPrimaryOpenRate to set
	 */
	public void setDeliPrimaryOpenRate(String deliPrimaryOpenRate) {
		this.deliPrimaryOpenRate = deliPrimaryOpenRate;
	}

	/**
	 * @return the largePrimaryOpenRate
	 */
	public String getLargePrimaryOpenRate() {
		return largePrimaryOpenRate;
	}

	/**
	 * @param largePrimaryOpenRate the largePrimaryOpenRate to set
	 */
	public void setLargePrimaryOpenRate(String largePrimaryOpenRate) {
		this.largePrimaryOpenRate = largePrimaryOpenRate;
	}

	/**
	 * @return the smallPrimaryOpenRate
	 */
	public String getSmallPrimaryOpenRate() {
		return smallPrimaryOpenRate;
	}

	/**
	 * @param smallPrimaryOpenRate the smallPrimaryOpenRate to set
	 */
	public void setSmallPrimaryOpenRate(String smallPrimaryOpenRate) {
		this.smallPrimaryOpenRate = smallPrimaryOpenRate;
	}

	/**
	 * @return the noPrePrimaryCommisAmount
	 */
	public String getNoPrePrimaryCommisAmount() {
		return noPrePrimaryCommisAmount;
	}

	/**
	 * @param noPrePrimaryCommisAmount the noPrePrimaryCommisAmount to set
	 */
	public void setNoPrePrimaryCommisAmount(String noPrePrimaryCommisAmount) {
		this.noPrePrimaryCommisAmount = noPrePrimaryCommisAmount;
	}

	/**
	 * @return the deliPrimaryCommisAmount
	 */
	public String getDeliPrimaryCommisAmount() {
		return deliPrimaryCommisAmount;
	}

	/**
	 * @param deliPrimaryCommisAmount the deliPrimaryCommisAmount to set
	 */
	public void setDeliPrimaryCommisAmount(String deliPrimaryCommisAmount) {
		this.deliPrimaryCommisAmount = deliPrimaryCommisAmount;
	}

	/**
	 * @return the largePrimaryCommisAmount
	 */
	public String getLargePrimaryCommisAmount() {
		return largePrimaryCommisAmount;
	}

	/**
	 * @param largePrimaryCommisAmount the largePrimaryCommisAmount to set
	 */
	public void setLargePrimaryCommisAmount(String largePrimaryCommisAmount) {
		this.largePrimaryCommisAmount = largePrimaryCommisAmount;
	}

	/**
	 * @return the smallPrimaryCommisAmount
	 */
	public String getSmallPrimaryCommisAmount() {
		return smallPrimaryCommisAmount;
	}

	/**
	 * @param smallPrimaryCommisAmount the smallPrimaryCommisAmount to set
	 */
	public void setSmallPrimaryCommisAmount(String smallPrimaryCommisAmount) {
		this.smallPrimaryCommisAmount = smallPrimaryCommisAmount;
	}

	/**
	 * @return the secondStoreOpenRate
	 */
	public String getSecondStoreOpenRate() {
		return secondStoreOpenRate;
	}

	/**
	 * @param secondStoreOpenRate the secondStoreOpenRate to set
	 */
	public void setSecondStoreOpenRate(String secondStoreOpenRate) {
		this.secondStoreOpenRate = secondStoreOpenRate;
	}

	/**
	 * @return the secondStoreOpenAmount
	 */
	public String getSecondStoreOpenAmount() {
		return secondStoreOpenAmount;
	}

	/**
	 * @param secondStoreOpenAmount the secondStoreOpenAmount to set
	 */
	public void setSecondStoreOpenAmount(String secondStoreOpenAmount) {
		this.secondStoreOpenAmount = secondStoreOpenAmount;
	}

	/**
	 * @return the noPrePartitionUnitPrice1
	 */
	public String getNoPrePartitionUnitPrice1() {
		return noPrePartitionUnitPrice1;
	}

	/**
	 * @param noPrePartitionUnitPrice1 the noPrePartitionUnitPrice1 to set
	 */
	public void setNoPrePartitionUnitPrice1(String noPrePartitionUnitPrice1) {
		this.noPrePartitionUnitPrice1 = noPrePartitionUnitPrice1;
	}

	/**
	 * @return the noPrePartitionUnitPrice2
	 */
	public String getNoPrePartitionUnitPrice2() {
		return noPrePartitionUnitPrice2;
	}

	/**
	 * @param noPrePartitionUnitPrice2 the noPrePartitionUnitPrice2 to set
	 */
	public void setNoPrePartitionUnitPrice2(String noPrePartitionUnitPrice2) {
		this.noPrePartitionUnitPrice2 = noPrePartitionUnitPrice2;
	}

	/**
	 * @return the deliPartitionUnitPrice1
	 */
	public String getDeliPartitionUnitPrice1() {
		return deliPartitionUnitPrice1;
	}

	/**
	 * @param deliPartitionUnitPrice1 the deliPartitionUnitPrice1 to set
	 */
	public void setDeliPartitionUnitPrice1(String deliPartitionUnitPrice1) {
		this.deliPartitionUnitPrice1 = deliPartitionUnitPrice1;
	}

	/**
	 * @return the deliPartitionUnitPrice2
	 */
	public String getDeliPartitionUnitPrice2() {
		return deliPartitionUnitPrice2;
	}

	/**
	 * @param deliPartitionUnitPrice2 the deliPartitionUnitPrice2 to set
	 */
	public void setDeliPartitionUnitPrice2(String deliPartitionUnitPrice2) {
		this.deliPartitionUnitPrice2 = deliPartitionUnitPrice2;
	}

	/**
	 * @return the largePartitionUnitPrice1
	 */
	public String getLargePartitionUnitPrice1() {
		return largePartitionUnitPrice1;
	}

	/**
	 * @param largePartitionUnitPrice1 the largePartitionUnitPrice1 to set
	 */
	public void setLargePartitionUnitPrice1(String largePartitionUnitPrice1) {
		this.largePartitionUnitPrice1 = largePartitionUnitPrice1;
	}

	/**
	 * @return the largePartitionUnitPrice2
	 */
	public String getLargePartitionUnitPrice2() {
		return largePartitionUnitPrice2;
	}

	/**
	 * @param largePartitionUnitPrice2 the largePartitionUnitPrice2 to set
	 */
	public void setLargePartitionUnitPrice2(String largePartitionUnitPrice2) {
		this.largePartitionUnitPrice2 = largePartitionUnitPrice2;
	}

	/**
	 * @return the smallPartitionUnitPrice1
	 */
	public String getSmallPartitionUnitPrice1() {
		return smallPartitionUnitPrice1;
	}

	/**
	 * @param smallPartitionUnitPrice1 the smallPartitionUnitPrice1 to set
	 */
	public void setSmallPartitionUnitPrice1(String smallPartitionUnitPrice1) {
		this.smallPartitionUnitPrice1 = smallPartitionUnitPrice1;
	}

	/**
	 * @return the smallPartitionUnitPrice2
	 */
	public String getSmallPartitionUnitPrice2() {
		return smallPartitionUnitPrice2;
	}

	/**
	 * @param smallPartitionUnitPrice2 the smallPartitionUnitPrice2 to set
	 */
	public void setSmallPartitionUnitPrice2(String smallPartitionUnitPrice2) {
		this.smallPartitionUnitPrice2 = smallPartitionUnitPrice2;
	}

	/**
	 * @return the noPreSecondaryOpenAmount
	 */
	public String getNoPreSecondaryOpenAmount() {
		return noPreSecondaryOpenAmount;
	}

	/**
	 * @param noPreSecondaryOpenAmount the noPreSecondaryOpenAmount to set
	 */
	public void setNoPreSecondaryOpenAmount(String noPreSecondaryOpenAmount) {
		this.noPreSecondaryOpenAmount = noPreSecondaryOpenAmount;
	}

	/**
	 * @return the deliSecondaryOpenAmount
	 */
	public String getDeliSecondaryOpenAmount() {
		return deliSecondaryOpenAmount;
	}

	/**
	 * @param deliSecondaryOpenAmount the deliSecondaryOpenAmount to set
	 */
	public void setDeliSecondaryOpenAmount(String deliSecondaryOpenAmount) {
		this.deliSecondaryOpenAmount = deliSecondaryOpenAmount;
	}

	/**
	 * @return the largeSecondaryOpenAmount
	 */
	public String getLargeSecondaryOpenAmount() {
		return largeSecondaryOpenAmount;
	}

	/**
	 * @param largeSecondaryOpenAmount the largeSecondaryOpenAmount to set
	 */
	public void setLargeSecondaryOpenAmount(String largeSecondaryOpenAmount) {
		this.largeSecondaryOpenAmount = largeSecondaryOpenAmount;
	}

	/**
	 * @return the smallSecondaryOpenAmount
	 */
	public String getSmallSecondaryOpenAmount() {
		return smallSecondaryOpenAmount;
	}

	/**
	 * @param smallSecondaryOpenAmount the smallSecondaryOpenAmount to set
	 */
	public void setSmallSecondaryOpenAmount(String smallSecondaryOpenAmount) {
		this.smallSecondaryOpenAmount = smallSecondaryOpenAmount;
	}

	/**
	 * @return the noPreSecondaryOpenRate
	 */
	public String getNoPreSecondaryOpenRate() {
		return noPreSecondaryOpenRate;
	}

	/**
	 * @param noPreSecondaryOpenRate the noPreSecondaryOpenRate to set
	 */
	public void setNoPreSecondaryOpenRate(String noPreSecondaryOpenRate) {
		this.noPreSecondaryOpenRate = noPreSecondaryOpenRate;
	}

	/**
	 * @return the deliSecondaryOpenRate
	 */
	public String getDeliSecondaryOpenRate() {
		return deliSecondaryOpenRate;
	}

	/**
	 * @param deliSecondaryOpenRate the deliSecondaryOpenRate to set
	 */
	public void setDeliSecondaryOpenRate(String deliSecondaryOpenRate) {
		this.deliSecondaryOpenRate = deliSecondaryOpenRate;
	}

	/**
	 * @return the largeSecondaryOpenRate
	 */
	public String getLargeSecondaryOpenRate() {
		return largeSecondaryOpenRate;
	}

	/**
	 * @param largeSecondaryOpenRate the largeSecondaryOpenRate to set
	 */
	public void setLargeSecondaryOpenRate(String largeSecondaryOpenRate) {
		this.largeSecondaryOpenRate = largeSecondaryOpenRate;
	}

	/**
	 * @return the smallSecondaryOpenRate
	 */
	public String getSmallSecondaryOpenRate() {
		return smallSecondaryOpenRate;
	}

	/**
	 * @param smallSecondaryOpenRate the smallSecondaryOpenRate to set
	 */
	public void setSmallSecondaryOpenRate(String smallSecondaryOpenRate) {
		this.smallSecondaryOpenRate = smallSecondaryOpenRate;
	}

	/**
	 * @return the noPreTotalOpenRate
	 */
	public String getNoPreTotalOpenRate() {
		return noPreTotalOpenRate;
	}

	/**
	 * @param noPreTotalOpenRate the noPreTotalOpenRate to set
	 */
	public void setNoPreTotalOpenRate(String noPreTotalOpenRate) {
		this.noPreTotalOpenRate = noPreTotalOpenRate;
	}

	/**
	 * @return the deliTotalOpenRate
	 */
	public String getDeliTotalOpenRate() {
		return deliTotalOpenRate;
	}

	/**
	 * @param deliTotalOpenRate the deliTotalOpenRate to set
	 */
	public void setDeliTotalOpenRate(String deliTotalOpenRate) {
		this.deliTotalOpenRate = deliTotalOpenRate;
	}

	/**
	 * @return the largeTotalOpenRate
	 */
	public String getLargeTotalOpenRate() {
		return largeTotalOpenRate;
	}

	/**
	 * @param largeTotalOpenRate the largeTotalOpenRate to set
	 */
	public void setLargeTotalOpenRate(String largeTotalOpenRate) {
		this.largeTotalOpenRate = largeTotalOpenRate;
	}

	/**
	 * @return the smallTotalOpenRate
	 */
	public String getSmallTotalOpenRate() {
		return smallTotalOpenRate;
	}

	/**
	 * @param smallTotalOpenRate the smallTotalOpenRate to set
	 */
	public void setSmallTotalOpenRate(String smallTotalOpenRate) {
		this.smallTotalOpenRate = smallTotalOpenRate;
	}

	/**
	 * @return the noPreTotalOpenAmount
	 */
	public String getNoPreTotalOpenAmount() {
		return noPreTotalOpenAmount;
	}

	/**
	 * @param noPreTotalOpenAmount the noPreTotalOpenAmount to set
	 */
	public void setNoPreTotalOpenAmount(String noPreTotalOpenAmount) {
		this.noPreTotalOpenAmount = noPreTotalOpenAmount;
	}

	/**
	 * @return the deliTotalOpenAmount
	 */
	public String getDeliTotalOpenAmount() {
		return deliTotalOpenAmount;
	}

	/**
	 * @param deliTotalOpenAmount the deliTotalOpenAmount to set
	 */
	public void setDeliTotalOpenAmount(String deliTotalOpenAmount) {
		this.deliTotalOpenAmount = deliTotalOpenAmount;
	}

	/**
	 * @return the largeTotalOpenAmount
	 */
	public String getLargeTotalOpenAmount() {
		return largeTotalOpenAmount;
	}

	/**
	 * @param largeTotalOpenAmount the largeTotalOpenAmount to set
	 */
	public void setLargeTotalOpenAmount(String largeTotalOpenAmount) {
		this.largeTotalOpenAmount = largeTotalOpenAmount;
	}

	/**
	 * @return the smallTotalOpenAmount
	 */
	public String getSmallTotalOpenAmount() {
		return smallTotalOpenAmount;
	}

	/**
	 * @param smallTotalOpenAmount the smallTotalOpenAmount to set
	 */
	public void setSmallTotalOpenAmount(String smallTotalOpenAmount) {
		this.smallTotalOpenAmount = smallTotalOpenAmount;
	}

	/**
	 * @return the calUnitPriceParcel
	 */
	public String getCalUnitPriceParcel() {
		return calUnitPriceParcel;
	}

	/**
	 * @param calUnitPriceParcel the calUnitPriceParcel to set
	 */
	public void setCalUnitPriceParcel(String calUnitPriceParcel) {
		this.calUnitPriceParcel = calUnitPriceParcel;
	}

	/**
	 * @return the calUnitPriceForehead
	 */
	public String getCalUnitPriceForehead() {
		return calUnitPriceForehead;
	}

	/**
	 * @param calUnitPriceForehead the calUnitPriceForehead to set
	 */
	public void setCalUnitPriceForehead(String calUnitPriceForehead) {
		this.calUnitPriceForehead = calUnitPriceForehead;
	}
}
