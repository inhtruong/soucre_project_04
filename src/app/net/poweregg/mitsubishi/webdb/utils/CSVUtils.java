package net.poweregg.mitsubishi.webdb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.poweregg.util.StringUtils;

public class CSVUtils {
	public static String removeDoubleQuote(String value) {
		return value.replace("\"", "");
	}

	public static List<String[]> readAllToListString(File csvFile, String splitSymbol, String encoding,
			boolean removeDoubleQuote) throws Exception {
		List<String[]> results = (List) new ArrayList<>();
		FileInputStream is = null;
		InputStreamReader isr = null;
		BufferedReader buff = null;
		try {
			is = new FileInputStream(csvFile.getPath());
			isr = new InputStreamReader(is, encoding);
			buff = new BufferedReader(isr);
			boolean first = true;
			while (buff.ready()) {
				String s = buff.readLine();
				if (first || StringUtils.nullBlankSpace(s)) {
					if (first)
						first = false;
					continue;
				}
				if (removeDoubleQuote) {
					results.add(removeDoubleQuote(s).split(splitSymbol, -1));
					continue;
				}
				results.add(s.split(splitSymbol, -1));
			}
		} finally {
			if (null != buff)
				try {
					buff.close();
				} catch (IOException iOException) {
				}
			if (null != isr)
				try {
					isr.close();
				} catch (IOException iOException) {
				}
			if (null != is)
				try {
					is.close();
				} catch (IOException iOException) {
				}
		}
		return results;
	}

	public static List<Map<String, String>> readAllToListMap(File csvFile, String splitSymbol, String encoding,
			boolean removeDoubleQuote) throws Exception {
		List<Map<String, String>> results = new ArrayList<>();
		FileInputStream is = null;
		InputStreamReader isr = null;
		BufferedReader buff = null;
		try {
			is = new FileInputStream(csvFile.getPath());
			isr = new InputStreamReader(is, encoding);
			buff = new BufferedReader(isr);
			boolean first = true;
			String[] header = null;
			while (buff.ready()) {
				String s = buff.readLine();
				if (first || StringUtils.nullBlankSpace(s)) {
					if (first) {
						header = removeDoubleQuote(s).split(splitSymbol, -1);
						first = false;
					}
					continue;
				}
				if (removeDoubleQuote) {
					String[] arrayOfString = removeDoubleQuote(s).split(splitSymbol, -1);
					Map<String, String> map = new HashMap<>();
					for (int j = 0; j < header.length; j++)
						map.put(header[j], arrayOfString[j]);
					results.add(map);
					continue;
				}
				String[] content = s.split(splitSymbol, -1);
				Map<String, String> mapContent = new HashMap<>();
				for (int i = 0; i < header.length; i++)
					mapContent.put(header[i], content[i]);
				results.add(mapContent);
			}
		} finally {
			if (null != buff)
				try {
					buff.close();
				} catch (IOException iOException) {
				}
			if (null != isr)
				try {
					isr.close();
				} catch (IOException iOException) {
				}
			if (null != is)
				try {
					is.close();
				} catch (IOException iOException) {
				}
		}
		return results;
	}
}
