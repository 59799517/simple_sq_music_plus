package com.sqmusicplus.v3.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件处理工具类
 *
 * @author ruoyi
 */
public class FileUtils
{

    /**
     * 整理文件（移动）
     * @param file
     * @param target
     * @return
     */
    public static File  organizeFiles(File file,File target){
        Path move =target.toPath();
        String musicPath = file.getPath();
        //判断最后一个符号是文件分隔符
        if(!musicPath.endsWith(File.separator)){
            musicPath = musicPath + File.separator;
        }

        if(!file.exists()){
            file.mkdirs();
        }
        if(file.exists()){
            //移动文件
            move  = FileUtil.move(file.toPath(), target.toPath(), false);
        }
        return move.toFile();
    }

    public static File  organizeFiles(File file,String newFilepath,String newFileName){
        Assert.notNull(newFilepath, "路径不能为空");
        Assert.notNull(newFileName, "文件名称不能为空");
        //判断最后一个符号是文件分隔符
        if(!newFilepath.endsWith(File.separator)){
            newFilepath = newFilepath + File.separator;
        }
        File target = new File(newFilepath + newFileName);
        return organizeFiles(file,target);
    }

    /**
     * 标准化用户输入的路径，处理不同操作系统间的路径分隔符差异
     * @param inputPath 用户输入的路径
     * @return 标准化后的路径
     */
    public static String normalizePath(String inputPath) {
        if (inputPath == null || inputPath.isEmpty()) {
            return inputPath;
        }
        
        // 将路径中的所有反斜杠和斜杠统一处理
        // 先统一替换为斜杠，再根据当前系统转换
        String normalized = inputPath.replace('\\', '/');
        
        // 使用 Paths.get 来处理路径标准化
        // 这会自动处理当前系统的路径分隔符
        try {
            Path path = Paths.get(normalized).normalize();
            return path.toString();
        } catch (Exception e) {
            // 如果 Paths.get 处理失败，回退到手动处理
            return normalized.replace('/', File.separatorChar);
        }
    }

    /**
     * 将用户输入的路径转换为当前系统格式
     * @param inputPath 用户输入的路径（可能来自 Windows 或 Linux）
     * @return 适用于当前操作系统的路径格式
     */
    public static String convertToSystemPath(String inputPath) {
        if (inputPath == null || inputPath.isEmpty()) {
            return inputPath;
        }
        
        // 替换所有可能的分隔符为当前系统的分隔符
        return inputPath.replace('/', File.separatorChar)
                       .replace('\\', File.separatorChar);
    }

    public static File findFile(String path,String fileName){
        try {
            File file = new File(path);
            if(file.exists()){
                File[] files = file.listFiles();
                for(File f:files){
                    //判断文件的名称忽略后缀
                    String name = f.getName().substring(0,f.getName().lastIndexOf("."));
                    if(name.equals(fileName)){
                        return f;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 获取文件后缀
     * @param name 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String name) {
        int index = name.lastIndexOf(".");
        return index == -1 ? "" : name.substring(index + 1);
    }
}