package net.poweregg.mitsubishi.service;

import javax.ejb.Local;

import net.poweregg.mitsubishi.dto.Umb01Dto;

@Local
public interface MitsubishiService {

	public Umb01Dto getDataMitsubishi(String dataNo) throws Exception;
}
