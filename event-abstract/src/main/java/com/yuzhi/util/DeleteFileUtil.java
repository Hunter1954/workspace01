package com.yuzhi.util;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 删除文件和目录
 *
 */
public class DeleteFileUtil {
	/**
     * 删除自动产生的Chinese_parser文件
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static void deleteChineseParser(String filePath) {
    	//获取目录名
    	File rootfile = new File(filePath);
    	if(!rootfile.isDirectory()){
    		System.out.println("传入的参数非文件夹！");
    		return;
    	}
    	File[] files = rootfile.listFiles();
    	//遍历删除文件
    	for(File file: files){
    		if(!file.getName().startsWith("__Chinese_Parser")){
    			continue;
    		}
    		file.delete();
    	}
    }
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static void deleteRecursively(String filePath) {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	String format = sdf.format(new Date());
    	
    	//获取目录名
    	File rootfile = new File(filePath);
    	if(!rootfile.isDirectory()){
    		System.out.println("传入的参数非文件夹！");
    		return;
    	}
    	File[] files = rootfile.listFiles();
    	//遍历删除文件
    	for(File file: files){
    		if(file.getName().contains(format)){
//    			System.out.println("当前文件包含今天的日期"+file.getName());
    			continue;
    		}
    		file.delete();
    	}
    }
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static void deleteLog4jRecursively(String filePath) {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	String format = sdf.format(new Date());
    	
    	//获取目录名
    	File rootfile = new File(filePath);
    	if(!rootfile.isDirectory()){
    		System.out.println("传入的参数非文件夹！");
    		return;
    	}
    	File[] files = rootfile.listFiles();
    	//遍历删除文件
    	for(File file: files){
    		String name = file.getName();
    		name=name.substring(name.indexOf("eventAbstract.log"+18));
    		if(name!=null&&!"".equals(name)){
//    			if(name.){
////    			System.out.println("当前文件包含今天的日期"+file.getName());
//    				continue;
//    			}
    		}
    		file.delete();
    	}
    }
    
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
    	File file = new File(fileName);
    	if (!file.exists()) {
//    		System.out.println("文件:" + fileName + "不存在！");
    		return false;
    	} else {
    		if (file.isFile())
    			return deleteFile(fileName);
    		else
    			return deleteDirectory(fileName);
    	}
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
//            System.out.println("文件:" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = DeleteFileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = DeleteFileUtil.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
//  // 删除单个文件
//  String file = "c:/test/test.txt";
//  DeleteFileUtil.deleteFile(file);
//  System.out.println();
        // 删除一个目录
        String dir = "D:/home/web/upload/upload/files";
        DeleteFileUtil.deleteDirectory(dir);
//  System.out.println();
//  // 删除文件
//  dir = "c:/test/test0";
//  DeleteFileUtil.delete(dir);

    }

}