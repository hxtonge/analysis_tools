package com.example.analysis;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AnalysisFiles
 *
 * @author zhangshun
 * @date 2022/8/19
 */
@Service
@Slf4j
public class AnalysisFiles {
    public static final Set<String> set = new HashSet<>();

    public static int count = 0;

    public static final String TABLE_PREFIX = "t_nbs_";

    /**
     * 入口
     *
     * @author zhangShun 2022/8/22
     */
    public void run(String path){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> AnalysisFiles run!");
        readFilePath(path);
        log.info("AnalysisFiles value: " +set);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> AnalysisFiles end!");
    }

    /**
     * 读取路径并解析
     *
     * @author zhangShun 2022/8/22
     */
    public void readFilePath(String path){
        File[] files = FileUtil.ls(path);
        for(File f : files){
            readFile(f);
        }
    }

    /**
     * 解析路径
     *
     * @author zhangShun 2022/8/22
     */
    public void readFile(File file){
        if(file.isDirectory()){
            readFilePath(file.getAbsolutePath());
        }else {
            count++;
            log.info("#"+count+" files read "+file.getAbsolutePath());
            if(filterRules(file.getName())){
                fileAnalysis(file);
            }else {
                log.info("This file does not need to be analyzed");
            }
        }
    }

    /**
     * 匹配要分析的文件名
     *
     * @author zhangShun 2022/8/22
     */
    private boolean filterRules(String name) {
        //return name.endsWith(".xml") || name.endsWith(".java");
        return name.endsWith(".xml");
    }

    /**
     * 文件分析
     *
     * @author zhangShun 2022/8/22
     */
    private void fileAnalysis(File file) {
        log.info("fileAnalysis begin:"+file.getName());
        FileReader fileReader = FileReader.create(file);
        String fileString = fileReader.readString();
        int[] allIndex = findAllIndex(fileString,TABLE_PREFIX);
        for (int index : allIndex) {
            int spaceIndex = StrUtil.indexOfIgnoreCase(fileString," ",index);
            int lineIndex = StrUtil.indexOfIgnoreCase(fileString,"\n",index);
            int enterIndex = StrUtil.indexOfIgnoreCase(fileString,"\r",index);
            int commaIndex = StrUtil.indexOfIgnoreCase(fileString,",",index);
            int leftParenthesisIndex = StrUtil.indexOfIgnoreCase(fileString,"(",index);
            int endIndex = getsTheSmallestValueInAPositiveInteger(spaceIndex,lineIndex,enterIndex,commaIndex,leftParenthesisIndex);
            String tableName = StrUtil.sub(fileString,index,endIndex);
            if(tableName.getBytes().length == tableName.length() && !tableName.contains("-")){
                set.add(StrUtil.sub(fileString,index,endIndex));
            }
        }
    }

    /**
     * 获取正整数中的最小值
     * 全为负数返回-1
     *
     * @author zhangShun 2022/8/22
     */
    private int getsTheSmallestValueInAPositiveInteger(int... number) {
        int min = -1;
        for (int i :number){
            if (i >= 0){
                if(min == -1){
                    min = i;
                }else {
                    min = Math.min(min, i);
                }
            }
        }
        return min;
    }

    private int[] findAllIndex(String string, String str) {
        List<Integer> list = new ArrayList<>();
        int index= 0;
        while (index >= 0){
            index = StrUtil.indexOfIgnoreCase(string,str,index);
            if(index > 0){
                list.add(index);
                index++;
            }
        }
        int[] inst = new int[list.size()];
        for(int i=0;i<list.size();i++) {
            inst[i] = list.get(i);
        }
        return inst;
    }
}
