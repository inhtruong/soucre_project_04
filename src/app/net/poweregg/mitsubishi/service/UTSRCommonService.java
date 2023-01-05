package net.poweregg.mitsubishi.service;

import java.util.List;

import javax.ejb.Local;

import net.poweregg.mitsubishi.dto.UTSR01SearchDto;

@Local
public interface UTSRCommonService {

	public List<UTSR01SearchDto> searchData(UTSR01SearchDto utsrSearchCond, long start, long max) throws Exception;

	public Long countTotalData(UTSR01SearchDto utsrSearchCond, long start, long max) throws Exception;
}
