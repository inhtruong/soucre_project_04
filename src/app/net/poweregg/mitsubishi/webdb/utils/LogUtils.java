package net.poweregg.mitsubishi.webdb.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.util.DateUtils;
import net.poweregg.util.StringUtils;

public class LogUtils {

	public LogUtils() {

	}

	/**
	 * ClassInfoリストから、classNoに対してClassInfoを取得する。
	 * 
	 * @param classInfos
	 * @param classNo
	 * @return
	 */
	public static ClassInfo getClassInfoByClassNo(List<ClassInfo> classInfos, String classNo) {
		return classInfos.stream().filter(classInfo -> classInfo.getClassno().equals(classNo)).findFirst().get();
	}

	/**
	 * ClassInfoリストから、classNoに対してcharData1を取得する。
	 * 
	 * @param classInfos
	 * @param classNo
	 * @param defaultVal
	 */
	public static String getCharData1(List<ClassInfo> classInfos, String classNo, String... defaultVal) {
		ClassInfo classInfo = LogUtils.getClassInfoByClassNo(classInfos, classNo);
		if (classInfo != null) {
			return classInfo.getChardata1();
		}
		if (defaultVal != null && defaultVal.length > 0) {
			return defaultVal[0];
		}
		return null;
	}

	/**
	 * 対象のフォルダを作成する
	 * 
	 * @param path
	 */
	public static void mkDir(String path) {
		File dir = new File(path);
		if (dir == null || dir.exists())
			return;
		dir.mkdirs();
	}

	/**
	 * ログを記載する。
	 * 
	 * @param logFileFullPath
	 * @param message
	 */
	public static void writeLog(String logFileFullPath, String batchCd, String message) {
		if (StringUtils.nullOrBlank(logFileFullPath)) {
			return;
		}
		try (FileWriter fw = new FileWriter(logFileFullPath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(DateUtils.formatDateString(new Date(), DateUtils.FULL_DATE_TIME_3) + " "
					+ MitsubishiConst.LEFT_BRACKET + batchCd + MitsubishiConst.RIGHT_BRACKET + message);
		} catch (IOException e) {
		}
	}

	/**
	 * ログファイルのパスを作成する。
	 * 
	 * @param settingInfoList
	 */
	public static String generateLogFileFullPath(List<ClassInfo> settingInfoList) {
		String folderLog = net.poweregg.mitsubishi.webdb.utils.LogUtils.getCharData1(settingInfoList,
				MitsubishiConst.CLASS_NO.CLASSNO_6.getValue());
		if (!folderLog.endsWith(MitsubishiConst.SEPARATOR)) {
			folderLog = folderLog + MitsubishiConst.SEPARATOR;
		}
		LogUtils.mkDir(folderLog);
		// String logFileName = LogUtils.getCharData1(settingInfoList,
		// MitsubishiConst.COMMON_NO_LOG0001.LOG_FILE_NAME.getValue());
		// return folderLog + logFileName;
		return folderLog;
	}
}
