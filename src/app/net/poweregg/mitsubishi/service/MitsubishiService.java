package net.poweregg.mitsubishi.service;

import java.util.List;

import javax.ejb.Local;

import org.json.JSONArray;
import org.json.JSONException;

import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.dto.UMB01MasterDto;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;

@Local
public interface MitsubishiService {

	/**
	 * 
	 * @param dataNo
	 * @param dbType
	 * @return
	 * @throws Exception
	 */
	public Umb01Dto getDataMitsubishi(String dataNo, int dbType) throws Exception;

	public Umb01Dto getDataUpdateStatus(int dbType, String customerCD, String destinationCD1,
			String destinationCD2, String productNameAbbreviation, String colorNo, String currencyCD,
			String clientBranchNumber, String priceForm, String managerNo) throws Exception;

	/**
	 * get table from table master of price
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<UMB01MasterDto> getDataPriceMaster() throws Exception;

	/**
	 * create XML table price
	 * 
	 * @return
	 */
	public String createXMLTablePrice(Umb01Dto umb01Dto);

	/**
	 * Update Record DB Temp
	 * 
	 * @param recordNo
	 * @param umb01Dto
	 * @return
	 * @throws Exception
	 */
	public void updateRecordDbPrice(String logFileFullPath, Umb01Dto umb01Dto, int dbType, String mode)
			throws Exception;
	
	public void registerRecordDbPrice(String logFileFullPath, Umb01Dto umb01Dto)
			throws Exception;

	/**
	 * Find Data UMB By Condition
	 * 
	 * @param webdbUtils
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public JSONArray findDataUmbByCondition(WebDbUtils webdbUtils, String field, String value, String order) throws Exception;
	
	public JSONArray findDataUpdateStatusByCondition(WebDbUtils webdbUtils, String customerCD,
			String destinationCD1, String destinationCD2, String productNameAbbreviation, String colorNo,
			String currencyCD, String clientBranchNumber, String priceForm, String managerNo) throws Exception;

	/**
	 * Get Info Web DB
	 * 
	 * @param dbType
	 * @return
	 */
	public List<ClassInfo> getInfoWebDb();

	public String exportCsvBtnStatusUMB01(Umb01Dto umb01Dto) throws JSONException, Exception;
}
