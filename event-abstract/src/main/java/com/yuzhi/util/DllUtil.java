package com.yuzhi.util;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.yuzhi.constant.DllConstant;

/**
 * 这是一个调用动态库的工具类			
 * @author Hunter
 *
 */
public class DllUtil {
	//编写一个interface接口，名字随便起
	//让此接口继承com.sun.jna.win32.StdCallLibrary接口
	public interface EventAbstract extends StdCallLibrary {
		//将c++提供的所有接口，用Java代码实现一下
		
		//HowNet初始化接口，对应c++接口列表里的 const bool initHowNet(char* ApPath);
		public boolean initHowNet(String Apath);
		//解析接口，对应c++接口列表里的 const bool parseText(char* ApPath);
		public boolean parseText(String input);
		//获取解析结果接口，对应c++接口列表里的 const char* getJsonRet();
		public String getJsonRet();
	}
	//预处理动态库
	public interface Preprocessor extends StdCallLibrary{
		//预处理方法
		public String format(String article);
	}
	
	//声明一个接口变量
	private static EventAbstract dllKernel;
	
	//预处理
	private static Preprocessor preprocessor;
	
	//加载预处理动态库
	public static Preprocessor getPreprocessor(){
		//加载本地dll接口
		if (preprocessor==null) {//如果变量不为空，说明已经加载，则直接返回
			//com.sun.jna.Native类有一个loadLibrary()方法，通过反射的方式来加载动态库
			preprocessor = (Preprocessor) Native.loadLibrary(DllConstant.getHownetPath()+"TextPreprocessor",
					Preprocessor.class);
		}
		//返回实例
		return preprocessor;
	};
	
	//创建接口实例的方法，相当于单例模式下的 getInstance()方法
	public static EventAbstract getDllKernel() {
		//加载本地dll接口
		if (dllKernel==null) {//如果变量不为空，说明已经加载，则直接返回
			//com.sun.jna.Native类有一个loadLibrary()方法，通过反射的方式来加载动态库
			dllKernel = (EventAbstract) Native.loadLibrary(DllConstant.getHownetPath()+"HowNet_ChineseParser_Helper",
				EventAbstract.class);
		}
		//返回实例
		return dllKernel;
	}
	
}
