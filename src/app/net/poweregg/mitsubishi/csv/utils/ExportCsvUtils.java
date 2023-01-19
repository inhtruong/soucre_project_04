package net.poweregg.mitsubishi.csv.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;

import net.poweregg.faces.util.DateUtils;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.dto.UMB01MasterDto;
import net.poweregg.mitsubishi.webdb.utils.ConvertUtils;
import net.poweregg.system.IllegalArgumentException;
import net.poweregg.util.FileUtils;
import net.poweregg.util.JSFUtil;
import net.poweregg.util.PESystemProperties;
import net.poweregg.util.StringUtils;

public class ExportCsvUtils {
	public static String exportCsvUSM11(List<UMB01MasterDto> rowDtos, String filePath) throws Exception {
		if (rowDtos == null || StringUtils.nullOrBlank(filePath)) {
			throw new IllegalArgumentException("Undefined data or file path");
		}

		OutputStreamWriter writer = null;
		FileOutputStream outStream = null;
		BufferedWriter buffWriter = null;

		try {
			File file = new File(filePath);
			outStream = new FileOutputStream(file, false);
			writer = getUTF8BOMwriter(outStream);
			buffWriter = new BufferedWriter(writer);

			StringBuilder builder = new StringBuilder(UMBCsvDto.getHeader());
			for (int i = 0; i < rowDtos.size(); i++) {
				UMB01MasterDto row = rowDtos.get(i);
				builder.append(MitsubishiConst.CR_LF);
				UMBCsvDto.addRowData(builder, row);

				if (i % 100 == 99 || i == rowDtos.size() - 1) {
					// Write and flush buffer to CSV file
					buffWriter.write(builder.toString());
					buffWriter.flush();
					builder = new StringBuilder();
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			FileUtils.close(buffWriter);
			FileUtils.close(writer);
			FileUtils.close(outStream);
		}

		return JSFUtil.createCsvFileUrl(filePath);
	}

	/**
	 * @param prefix
	 * @param ext
	 * @return file name
	 */
	public static String createFileName(String prefix, String ext, Long empId) {
		String tenPuDir = PESystemProperties.getInstance().getProperty(MitsubishiConst.TENPU_DIR);
		if (ConvertUtils.isNullOrEmptyOrBlank(tenPuDir)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(tenPuDir);
		buffer.append(MitsubishiConst.SEPARATOR);
		buffer.append(prefix);
		if (empId != null) {
			buffer.append(MitsubishiConst.UNDER_LINE);
			buffer.append(empId);
		}
		buffer.append(MitsubishiConst.UNDER_LINE);
		buffer.append(DateUtils.formatDateString(Calendar.getInstance().getTime(), MitsubishiConst.YYYYMMDDHHMMSS));
		buffer.append(ext);
		return buffer.toString();
	}

	private static OutputStreamWriter getUTF8BOMwriter(FileOutputStream outStream) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(outStream, MitsubishiConst.UTF_8);
		writer.write(MitsubishiConst.UTF8_BOM);
		return writer;
	}
}
