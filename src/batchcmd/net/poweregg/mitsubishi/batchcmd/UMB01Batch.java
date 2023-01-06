package net.poweregg.mitsubishi.batchcmd;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author diennv
 * @Create date: 2022/12/28
 */

public class UMB01Batch {

	public UMB01Batch() {
		super();
	}

	private static final String LOG_BEGIN = "Begin";
	private static final String LOG_FINISH = "Finish";

	public static void main(String[] args) {
		System.out.println(LOG_BEGIN);

		if (args.length < 2) {
			System.out.print("工事未登録データ登録");
			System.out.println("args must be: connectURL csvFilePath");
			System.out.println(" Example: UMB01Batch http://localhost:9090/pe4j C:\\CsvFilePath");
			System.exit(101);
		}
		final String jsfPath = "/UAP/UMB/UMB0101e.jsf";
		try {
			String filePath = args[1].trim();
			String extName = filePath.substring(filePath.length() - 4, filePath.length());
			boolean isCSV = ".csv".equalsIgnoreCase(extName);
			if (!isCSV) {
				System.out.println("取込ファイルをCSVとして選択してください。");
				return;
			}
			String connectURL = trim(args[0]);
			StringBuilder urlStr = new StringBuilder();
			urlStr.append(connectURL);
			urlStr.append(jsfPath);
			urlStr.append("?csvFilePath=" + URLEncoder.encode(args[1].trim(), "UTF-8"));
			URL url = new URL(urlStr.toString());
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);

			PrintStream outStream = new PrintStream(conn.getOutputStream());
			outStream.flush();
			outStream.close();

			DataInputStream inStream = new DataInputStream(conn.getInputStream());
			int inputLine;
			StringBuilder inputLineBuilder = new StringBuilder();
			while ((inputLine = inStream.read()) != -1) {
				inputLineBuilder.append((char) inputLine);
			}
			inStream.close();

			String returnCode = inputLineBuilder
					.substring(inputLineBuilder.indexOf("</head>") + 7, inputLineBuilder.indexOf("</html>")).trim();

			if ("0".equals(returnCode)) {
				System.out.println(LOG_FINISH);
			} else {
				System.out.println(returnCode);
			}
			System.exit(Integer.parseInt(returnCode));

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * trim string
	 * 
	 * @param obj
	 * @return
	 */
	private static String trim(String obj) {
		return (obj != null) ? obj.toString().trim() : null;
	}
}
