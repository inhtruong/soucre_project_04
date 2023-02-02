package net.poweregg.mitsubishi.service;

import java.util.List;

import javax.ejb.Local;

import org.json.JSONArray;

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
	
	/**
	 * get table from table master of price
	 * @return
	 * @throws Exception
	 */
	public List<UMB01MasterDto> getDataPriceMaster() throws Exception;
	
	/**
	 * create XML table price
	 * @return
	 */
	public String createXMLTablePrice(Umb01Dto umb01Dto);
	
	/**
	 * Update Record DB Temp
	 * @param recordNo
	 * @param appRecepNo
	 * @param statusCd
	 * @return
	 * @throws Exception
	 */
	public void updateRecordDbPrice(String recordNo, String appRecepNo, String statusCd, int dbType, String mode) throws Exception;

	/**
	 * Find Data UMB By Condition
	 * @param webdbUtils
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public JSONArray findDataUmbByCondition(WebDbUtils webdbUtils, String field, String value) throws Exception;

	/**
	 * Get Info Web DB
	 * @param dbType
	 * @return
	 */
	public List<ClassInfo> getInfoWebDb();
	
}
