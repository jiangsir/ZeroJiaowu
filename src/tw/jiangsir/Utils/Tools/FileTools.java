package tw.jiangsir.Utils.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.TreeMap;

import tw.jiangsir.Utils.Exceptions.DataException;

public class FileTools {
	/**
	 * 取得指定 path 下的所有檔案, 並進入目錄底下尋找
	 * 
	 * @param path
	 * @return
	 */
	public ArrayList<File> findFiles(File path, String regex) {
		ArrayList<File> files = new ArrayList<File>();
		for (File file : path.listFiles()) {
			System.out.print("[" + file.getPath() + "] ");
			int length = String.valueOf(file.getPath()).length() + 4;
			while (length-- > 0) {
				System.out.print('\b');
			}

			// 先列出目錄
			if (file.isDirectory() && file.canRead()) { // 是否為目錄
				// 取得路徑名
				files.addAll(findFiles(file, regex));
			} else if (file.isFile() && file.toString().matches(regex)) {
				// 檔案先存入fileList，待會再列出
				System.out.println(file.toString());
				files.add(file);
			}
		}
		// 列出檔案
		// for (File f : fileList) {
		// System.out.println(f.toString());
		// }
		// System.out.println("共獲得 " + fileList.size() + " 個檔案");
		return files;
	}

	/**
	 * 建立檔案 包括 測資檔 及 .cpp 檔
	 * 
	 * @param file
	 * @param data
	 * @throws IOException
	 */
	public void createfile(File file, String data) throws DataException {
		try {
			BufferedWriter outfile = null;
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			outfile = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			data = data.replaceAll("\n", System.getProperty("line.separator"));
			outfile.write(data);
			// outfile.newLine(); // qx 換行
			outfile.flush();
			outfile.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(file + "檔案新增失敗！！");
			throw new DataException(file.getName() + "檔案新增失敗！！", e);
		}
		System.gc();
	}

	/**
	 * 取得指定目錄下的檔案, 不含目錄
	 */
	public TreeMap<String, Long> getFilenames(String path, String regex) {
		File file = new File(path);
		TreeMap<String, Long> fileList = new TreeMap<String, Long>();
		if (!file.exists()) {
			return fileList;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filestring = files[i].toString();
			// 先列出目錄
			if (!files[i].isDirectory() && filestring.matches(regex)) { // 是否為目錄
				String filename = filestring.substring(filestring
						.lastIndexOf(System.getProperty("file.separator")) + 1);
				fileList.put(filename, new File(path, filename).length());
			}
		}
		return fileList;
	}

	// public String readFileByString(File file) {
	// try {
	// return Files.toString(file, Charset.defaultCharset());
	// } catch (IOException e) {
	// e.printStackTrace();
	// return "";
	// }
	// }

	public String readFile(File file, int MAX_SIZE) {
		// 所有跟 readfile 有關的都要改成 path 由內部指定。外部只允許指定 filename
		// filename = filename.replaceAll("\\.\\.", "");
		// path = path.replaceAll("\\.\\.", "");
		// path = Utils.parsePath(path);
		// File file = new File(path + filename);
		String line = null;

		StringBuffer text = new StringBuffer(MAX_SIZE);
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader breader = new BufferedReader(new InputStreamReader(
					fis, "UTF-8"));
			while ((line = breader.readLine()) != null) {
				text.append(line + "\n");
				if (text.length() >= MAX_SIZE) {
					// throw new DataException("超過 " + MAX_SIZE
					// + " Bytes，以下略過.....");
					text.append("超過 " + MAX_SIZE + " Bytes，以下略過.....");
					break;
				}
			}
			fis.close();
			breader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text.toString();
	}

	public int readFilelinecount(String path, String filename) {
		// 所有跟 readfile 有關的都要改成 path 由內部指定。外部只允許指定 filename
		filename = filename.replaceAll("\\.\\.", "");
		path = path.replaceAll("\\.\\.", "");
		if (!path.endsWith(System.getProperty("file.separator"))) {
			path = path + System.getProperty("file.separator");
		}
		int lines = 0;
		try {
			FileInputStream fis = new FileInputStream(path + filename);
			BufferedReader breader = new BufferedReader(new InputStreamReader(
					fis, "UTF-8"));
			while ((breader.readLine()) != null) {
				lines++;
			}
			fis.close();
			breader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public ArrayList<String> readFilelines(String path, String filename,
			String encode) {
		// 所有跟 readfile 有關的都要改成 path 由內部指定。外部只允許指定 filename
		filename = filename.replaceAll("\\.\\.", "");
		path = path.replaceAll("\\.\\.", "");
		if (!path.endsWith(System.getProperty("file.separator"))) {
			path = path + System.getProperty("file.separator");
		}
		// int MAX_LENGTH = 1000000;
		String line = null;
		ArrayList<String> text = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(path + filename);
			BufferedReader breader = new BufferedReader(new InputStreamReader(
					fis, encode));
			while ((line = breader.readLine()) != null) {
				if (!"".equals(line.trim())) {
					text.add(line);
				}
			}
			fis.close();
			breader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

}
