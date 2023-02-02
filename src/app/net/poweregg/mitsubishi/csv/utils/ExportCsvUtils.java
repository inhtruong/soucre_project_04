package net.poweregg.mitsubishi.csv.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.system.IllegalArgumentException;
import net.poweregg.util.FileUtils;
import net.poweregg.util.JSFUtil;
import net.poweregg.util.StringUtils;

public class ExportCsvUtils {

	public static String exportCsvUMB01(JSONObject rowDto, String filePath) throws Exception {
		if (rowDto == null || StringUtils.nullOrBlank(filePath)) {
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
			builder.append(MitsubishiConst.CR_LF);
			UMBCsvDto.addRowData(builder, rowDto);
			// Write and flush buffer to CSV file
			buffWriter.write(builder.toString());
			buffWriter.flush();
			builder = new StringBuilder();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			FileUtils.close(buffWriter);
			FileUtils.close(writer);
			FileUtils.close(outStream);
		}

		return JSFUtil.createCsvFileUrl(filePath);
	}

	private static OutputStreamWriter getUTF8BOMwriter(FileOutputStream outStream) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(outStream, MitsubishiConst.UTF_8);
		writer.write(MitsubishiConst.UTF8_BOM);
		return writer;
	}
}
