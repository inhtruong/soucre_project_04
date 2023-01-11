package net.poweregg.mitsubishi.service;

import java.util.List;

import javax.ejb.Local;

import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.dto.UmitsubishiMasterDto;

@Local
public interface MitsubishiService {

	public Umb01Dto getDataMitsubishi(String dataNo) throws Exception;
	
	/**
	 * get table from table master of price
	 * @return
	 * @throws Exception
	 */
	public List<UmitsubishiMasterDto> getDataPriceMaster() throws Exception;
	
}
