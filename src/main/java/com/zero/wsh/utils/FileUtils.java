package com.zero.wsh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {
    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static File multFileToFile(MultipartFile multfile) {
        // 获取文件名
        String fileName = multfile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        File file = null;
        try {
            file = File.createTempFile(UUID.randomUUID().toString(), prefix);
            multfile.transferTo(file);
        } catch (IOException e) {
            log.error("文件转换失败", e);
        }
        return file;
    }
}
