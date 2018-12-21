package com.yuzhi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yuzhi.constant.DllConstant;
import com.yuzhi.dto.*;
import com.yuzhi.util.DeleteFileUtil;
import com.yuzhi.util.DllUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yuzhi.service.HownetDllService;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class HownetDllServiceImpl implements HownetDllService {


    //
    private static final Logger logger = LoggerFactory.getLogger(HownetDllServiceImpl.class);
    //定义HowNet时间抽取接口
    private static DllUtil.EventAbstract eventAbstract;
    //预处理动态库
    private static DllUtil.Preprocessor preprocessor;
    //	//定义读取触发词映射关系表的缓冲字符流
    private static BufferedReader relationMappingTableReader = null;
    //	//定义读取触发词映射关系表的缓冲字符流
    private static Map<String, String> relationMappingTableMap = null;

    //	/**
//	 * 所有公用的dll调用全部在项目启动时进行初始化
//	 */
    static {
        System.out.println(DllConstant.getHownetPath());
        //删除记录Json解析出错句子的文件
        DeleteFileUtil.delete(DllConstant.getHownetPath() + "sentenceFailToConvertToJson.txt");
        //删除记录没有触发词句子的文件
        DeleteFileUtil.delete(DllConstant.getHownetPath() + "sentenceWithoutTriger.txt");
        //获取调用本地dll解析器的api，并初始化
        eventAbstract = DllUtil.getDllKernel();
        preprocessor = DllUtil.getPreprocessor();
        System.out.println("预处理动态库加载>>>>>>" + preprocessor);
        System.out.println("抽取动态库加载>>>>>>" + eventAbstract);
        boolean init = eventAbstract.initHowNet(DllConstant.getHownetPath());
        System.out.println("抽取动态库初始化结果：" + init);
        //读取触发词映射关系表字符流
        try {
            relationMappingTableReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(DllConstant.getHownetPath() + "RelationMappingTable.txt")), "UTF-8"));
            //当前行,因为目前就一行，所以只获取一次就行了
            StringBuffer relationMappingTable = new StringBuffer();
            String currentLine = null;
            while ((currentLine = relationMappingTableReader.readLine()) != null) {
                relationMappingTable.append(currentLine.trim());
            }
            try {
                relationMappingTableMap = (Map<String, String>) JSON.parse(relationMappingTable.toString());
            } catch (Exception e) {
                System.out.println("触发词映射关系表出现语法错误，导致加载失败");
            }
            System.out.println("触发词映射关系表加载成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                relationMappingTableReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 事件主体抽取
     */
    @Override
    public List<Object> eventAbstract(String articleID, String article) {
        //创建返回的对象
        List<Object> resultEntities = new LinkedList<Object>();
        //入门判断
        if (articleID == null || "".equals(articleID)) {//ID不为空
            resultEntities.add("文章ID不能为空！");
            return resultEntities;
        }
        if (article == null || "".equals(article)) {//抽取文本不能为空
            resultEntities.add("文章内容不能为空！");
            return resultEntities;
        }
        //导致返回结果不正确的字符过滤
        article = article.replace("\"", "“");
        article = article.replace("\'", "’");
        article = article.replace("\\r", "\r");
        article = article.replace("\\n", "\n");
        article = article.replace("\\u201C", "“");
        article = article.replace("\\u201D", "”");
        article = article.replace("\\", "");

        //单句解析结束的文本
        String analyzeResult = "";
        //创建文章解析日志实例
        AnalyzeLogDTO analyzeLogDTO = null;
        List<CurrentSentenceDTO> currentSentenceEntities = new LinkedList<CurrentSentenceDTO>();

        //单句解析结束的文本
        logger.info("文章#" + articleID + "#预处理开始》》》》》");
        String format = preprocessor.format(article);
        logger.info("文章#" + articleID + "#预处理结束》》》》》");
        //先分句
        List<String> parseArray = null;
        try {
            parseArray = JSON.parseArray(format, String.class);
        } catch (Exception e) {
            System.out.println("预处理返回结果不正确，JSON无法解析！");
            resultEntities.add("当前文章预处理失败！");
            return resultEntities;
        }
        logger.info("当前文章解析开始》》》》》");
        //创建单句解析日志对象
        CurrentSentenceDTO currentSentenceDTO = null;
        for (String sentence : parseArray) {
            //所走规则列表
            List<RulesDTO> analyzeLog = new LinkedList<RulesDTO>();
            //分词列表
            List<KeenageDataDTO> nodesList = null;
            if (sentence.trim().length() >= 300) {
                analyzeLog.add(new RulesDTO("--", "该句文本在规定长度以内没有分句符，导致其解析失败！！"));
            } else {
                synchronized (this) {
                    boolean result = eventAbstract.parseText(sentence);
                    //开始解析
                    if (result) {
                        analyzeResult = eventAbstract.getJsonRet();
                    }
                }
                if (!"".equals(analyzeResult)) {
                    //解析结果转换成返回对象
                    SingleSentenceResultAndLogDTO singleSentenceResultAndLogDTO = this.getReturnJsonResultAndPrintLogDTO(sentence, analyzeResult);
                    //将当前句的解析结果，并入全文解析结果
                    if (singleSentenceResultAndLogDTO.getReturnJsonEntities() != null) {
                        resultEntities.add(new ResultDTO(sentence, singleSentenceResultAndLogDTO.getReturnJsonEntities()));
                    }
                    analyzeLog = singleSentenceResultAndLogDTO.getRulesEntities();
                    nodesList = singleSentenceResultAndLogDTO.getNodes();
                }
            }
            //创建单句解析结果实体类
            currentSentenceDTO = new CurrentSentenceDTO(sentence, nodesList, analyzeLog);
            //将解析日志序列化，然后打印
            String jsonStringSentence = JSON.toJSONString(currentSentenceDTO);
            logger.info(jsonStringSentence);
            currentSentenceEntities.add(currentSentenceDTO);
        }
        //创建文章解析日志对象
        analyzeLogDTO = new AnalyzeLogDTO(articleID, currentSentenceEntities);
        //写入日志
        String jsonStringArticle = JSON.toJSONString(analyzeLogDTO);
        logger.info(jsonStringArticle);
        logger.info("当前文章解析结束》》》》》");
        //得到最终结果，并返回
//		finalResult=JSON.toJSONString(returnJsonEntities);
        return resultEntities;
    }


    /**
     * 健康测试方法
     */
    @Override
    public String keepAlived() {
        boolean result = eventAbstract.parseText("这是一条测试语句。");
        if (result) {
            return "Alived";
        }
        return "HowNet is Dead!!";
    }


    /**
     * 将解析结果封装成返回数据类型
     *
     * @param originSentence
     * @param analyzeResult
     */
    public SingleSentenceResultAndLogDTO getReturnJsonResultAndPrintLogDTO(String originSentence, String analyzeResult) {
        //创建日志信息对象
        List<RulesDTO> rulesEntities = new LinkedList<RulesDTO>();
        //创建分词节点对象
        List<KeenageDataDTO> returnNodesList = new LinkedList<KeenageDataDTO>();
        //创建解析结果对象
        List<ReturnJsonDTO> returnJsonEntities = new LinkedList<ReturnJsonDTO>();
        //创建Gson对象
        //将返回的信息转换成数组形式
        AnalyzeResultDTO analyzeResultDTO = null;
        try {
            analyzeResultDTO = JSON.parseObject(analyzeResult, AnalyzeResultDTO.class);
        } catch (JSONException e) {
            this.writeIntoFileResultToJsonFailedSentence("当前句子有抽取结果，但是其JSON格式有错，导致Java应用提取失败！", originSentence, analyzeResult);
            //如果没转JSON成功，默认还是要增加词语索引
            return new SingleSentenceResultAndLogDTO(returnJsonEntities, rulesEntities, returnNodesList);
        }
        //获取分析结果对象列表
        List<SentenceDTO> sentences = analyzeResultDTO.getSentence();

        //遍历每一个对象（正常情况下，只有一个对象）
        for (SentenceDTO sentence : sentences) {
            //获取该句子对象下，所有的分词节点
            List<KeenageDataDTO> nodes = sentence.getNodes();
            //不同类型的事件分类
            Map<String, ReturnJsonDTO> differentEvent = new HashMap<String, ReturnJsonDTO>();
            //遍历分词节点
            for (KeenageDataDTO keenageData : nodes) {
                String key = keenageData.getKey();
                String[] splitKeys = key.split(",");
                //预处理
                for (String splitKey : splitKeys) {
                    if ("".equals(splitKey)) {
                        continue;
                    }
                    //判断是否为金融事件抽取关键词
                    if (!splitKey.contains("_")) {//如果没有，直接进行下一个
//						System.out.println("遇到非金融事件抽取标签："+splitKey);
                        continue;
                    }
                    if (!splitKey.matches("(mention|sub|obj|date|amount)\\w*")) {
//						System.out.println("遇到非金融事件主体标签："+splitKey);
                        continue;
                    }
                    //截取后半段以进行分类
                    String eventAbbreviation = splitKey.substring(splitKey.indexOf("_") + 1);
                    //截取"_"前半段以进行判断
                    //如果没有当前事件类，就创建
                    ReturnJsonDTO eventDTO = null;
                    if (differentEvent.get(eventAbbreviation) == null) {
                        String typeNodes = "";//触发词类型
                        List<String> triggerNodes = new LinkedList<String>();//触发词节点
                        List<String> subNodes = new LinkedList<String>();//事件主体节点
                        List<String> objNodes = new LinkedList<String>();//事件客体节点
                        List<String> dateNodes = new LinkedList<String>();//时间节点
                        List<String> amountNodes = new LinkedList<String>();//程度节点
                        List<List<Integer>> trigger_positions = new LinkedList<List<Integer>>();//
                        //创建新对象
                        eventDTO = new ReturnJsonDTO(typeNodes, triggerNodes, trigger_positions, subNodes, objNodes, dateNodes, amountNodes);
                    } else {
                        eventDTO = differentEvent.get(eventAbbreviation);
                    }
                    //根据key值更新返回实体类
                    eventDTO = this.upgradeReturnJsonDTO(splitKey, keenageData, eventDTO);
                    //重新放回到map中
                    differentEvent.put(eventAbbreviation, eventDTO);
                }//end-keys
            }//end-keenageDatas
            //获取该对象下所有的规则记录
            if (sentence != null && sentence.getRules() != null) {
                rulesEntities.addAll(sentence.getRules());
            }
            //获取该对象下所有的分词结果
            if (nodes != null) {
                returnNodesList.addAll(nodes);
            }
            //遍历map中的值
            for (ReturnJsonDTO returnJsonDTO : differentEvent.values()) {
                //判断时间是否有triggerType
                if (returnJsonDTO.getType() == null || "".equals(returnJsonDTO.getType())) {
                    String reason = "";
                    if (returnJsonDTO.getTrigger() == null || returnJsonDTO.getTrigger().isEmpty()) {
                        //没有触发词引起的没有触发词类型
                        reason = "###没有事件触发词引起的没有事件类型###";
                    } else {
                        //关系表中没有映射引起的没有触发词类型
                        reason = "###映射表中没有找到对应的事件类型###";
                    }
                    this.writeIntoFileSentenceWithoutMentionWord(reason, originSentence, nodes, returnJsonDTO);
                    continue;//如果没有直接跳过，不作为结果返回
                }
                returnJsonEntities.add(returnJsonDTO);//将map中的对象放入返回的列表中
            }
        }//end-Sentences

        //返回当前句子解析结果及日志对象
        return new SingleSentenceResultAndLogDTO(returnJsonEntities, rulesEntities, returnNodesList);
    }

    /**
     * 将有抽取结果但是Json格式有误的文本打印至本地
     *
     * @param reason//原因
     * @param originSentence//原句
     * @param analyzeResult//抽取及解析结果
     */
    private void writeIntoFileResultToJsonFailedSentence(String reason, String originSentence, String analyzeResult) {
        // TODO Auto-generated method stub
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(DllConstant.getHownetPath() + "sentenceFailToConvertToJson.txt"), true), "utf-8"));
//			bw.append("\r\n");
//			bw.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			bw.append("\r\n");
//			bw.append(reason);
            bw.append("\r\n");
//			bw.append("原句>>>>>>>>>>>"+originSentence);
            bw.append(originSentence);
            bw.append("\r\n");
//			bw.append("HowNet抽取结果>>>>>>>>>>>"+analyzeResult);
//			bw.append("\r\n");
//			bw.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            bw.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * 把没有触发词类型的句子写入固定文件，并说明原因
     *
     * @param reason         没有触发词类型的原因
     * @param originSentence 原句
     * @param nodes
     * @param returnJsonDTO  抽取结果
     */
    public void writeIntoFileSentenceWithoutMentionWord(String reason, String originSentence, List<KeenageDataDTO> nodes, ReturnJsonDTO returnJsonDTO) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(DllConstant.getHownetPath() + "sentenceWithoutTriger.txt"), true), "utf-8"));
            bw.append("\r\n");
            bw.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            bw.append("\r\n");
            bw.append(reason);
            bw.append("\r\n");
            bw.append("以下为原句和解析中所有打标签的项：");
            bw.append("\r\n");
            bw.append(originSentence);
            for (KeenageDataDTO keenageData : nodes) {
                String expression = keenageData.getExpression();
                String key = keenageData.getKey();
                if (key == null || "".equals(key)) {
                    continue;
                }
                bw.append("\r\n");
                bw.append(expression + ">>>>>>>>>>>" + key);
            }
            bw.append("\r\n");
            bw.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            bw.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    /**
     * 判断并更新节点
     *
     * @param splitKey
     * @param keenageData
     * @param returnJsonDTO
     * @return
     */
    public ReturnJsonDTO upgradeReturnJsonDTO(String splitKey, KeenageDataDTO keenageData, ReturnJsonDTO returnJsonDTO) {
        //获取触发词的分词
        String expression = keenageData.getExpression();
        String keyType = splitKey.substring(0, splitKey.indexOf("_"));
        switch (keyType) {
            case DllConstant.KEY_MENTION://触发词
                //先存入触发词
                List<String> triggerNodes = returnJsonDTO.getTrigger();
                triggerNodes.add(expression);
                returnJsonDTO.setTrigger(triggerNodes);
                //获取触发词类型
                String triggerType = this.getTriggerType(splitKey);
                if (triggerType != null && !"".equals(triggerType)) {
                    //设置触发词类型
                    String typeNodes = returnJsonDTO.getType();
                    typeNodes = triggerType;
                    returnJsonDTO.setType(typeNodes);
                } else {
                    System.out.println("当前key没有在映射表中找到对应的触发词类型：" + splitKey);
                }
                //设置触发词位置
                List<List<Integer>> trigger_positions = returnJsonDTO.getTrigger_position();
                List<Integer> triggerPosition = new LinkedList<Integer>();
                try {
                    triggerPosition.add(Integer.parseInt(keenageData.getPos_begin()));
                    triggerPosition.add(Integer.parseInt(keenageData.getPos_end()));
                } catch (Exception e) {
                    System.out.println("触发词定位失败，ParseInt异常");
                }
                trigger_positions.add(triggerPosition);
                returnJsonDTO.setTrigger_position(trigger_positions);
                break;
            case DllConstant.KEY_SUB://主体
                List<String> subNodes = returnJsonDTO.getSub();
                subNodes.add(expression);
                returnJsonDTO.setSub(subNodes);
                break;
            case DllConstant.KEY_OBJ://客体
                List<String> objNodes = returnJsonDTO.getObj();
                objNodes.add(expression);
                returnJsonDTO.setObj(objNodes);
                break;
            case DllConstant.KEY_DATE://时间
                List<String> dateNodes = returnJsonDTO.getDate();
                dateNodes.add(expression);
                returnJsonDTO.setDate(dateNodes);
                break;
            case DllConstant.KEY_AMOUNT://程度
                List<String> amountNodes = returnJsonDTO.getAmount();
                amountNodes.add(expression);
                returnJsonDTO.setAmount(amountNodes);
                break;
        }

        return returnJsonDTO;
    }

    /**
     * 从映射表里面获取对应的触发词类型
     *
     * @param key
     * @return
     */
    public String getTriggerType(String key) {
        String string = relationMappingTableMap.get(key);
        return string;
    }


    /**
     * 预处理分句(注意：分句中可能含无内容分句)
     *
     * @param article
     * @return
     */
    public String[] splitParagrah(String article) {
        String[] result = null;
        article = article.replaceAll("。", "。（Fstp）");
        article = article.replaceAll("？", "？（Fstp）");
        article = article.replaceAll("！", "！（Fstp）");
        article = article.replaceAll("；", "；（Fstp）");
        String regex = "(\r|\n|\0|（Fstp）)";
        result = article.split(regex);
        return result;
    }

}
