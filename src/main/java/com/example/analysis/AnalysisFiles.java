package com.example.analysis;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
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

    public void run(String path){
        log.info("AnalysisTables run!");
        readFilePath(path);
        log.info("AnalysisTables end!");
    }

    public void readFilePath(String path){
        File[] files = FileUtil.ls(path);
        for(File f : files){
            readFile(f);
        }
    }

    public void readFile(File file){
        if(file.isDirectory()){
            readFilePath(file.getAbsolutePath());
        }else {
            count++;
            log.info("#"+count+" files read "+file.getAbsolutePath());
            if(file.getName().endsWith(".xml") || file.getName().endsWith(".java")){
                fileAnalysis(file);
            }else {
                log.info("This file does not need to be analyzed");
            }
        }
    }

    private void fileAnalysis(File file) {
        log.info("file name:"+file.getName());
    }
}
