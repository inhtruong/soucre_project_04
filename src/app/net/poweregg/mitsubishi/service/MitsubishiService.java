package net.poweregg.mitsubishi.service;

import java.util.List;

import javax.ejb.Local;

import org.json.JSONArray;

import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.dto.PriceCalParam;
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.dto.UmitsubishiMasterDto;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;

@Local
public interface MitsubishiService {

	public Umb01Dto getDataMitsubishi(String dataNo) throws Exception;
	
	/**
	 * get table from table master of price
	 * @return
	 * @throws Exception
	 */
	public List<UmitsubishiMasterDto> getDataPriceMaster() throws Exception;
	
	/**
	 * create XML table price
	 * @return
	 */
	public String createXMLTablePrice(Umb01Dto umb01Dto);
	
	public void updateRecordDbTemp(String recordNo, String appRecepNo, String state) throws Exception;

	public JSONArray findDataUmbByCondition(WebDbUtils webdbUtils, String field, String value) throws Exception;

	public List<ClassInfo> getInfoWebDb();
	
}
