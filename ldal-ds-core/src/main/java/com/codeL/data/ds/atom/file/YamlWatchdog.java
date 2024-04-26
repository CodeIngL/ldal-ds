package com.codeL.data.ds.atom.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class YamlWatchdog extends FileWatchdog {

    private static final Logger logger = LoggerFactory.getLogger(YamlWatchdog.class);

    public YamlWatchdog(String filename, FileReloader reloader) {
        super(filename,reloader);
    }

    public void doOnChange() {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            String content = FileUtils.readFileToString(file, "UTF-8");
            if (content == null || content.length() == 0) {
                logger.error("please config file config of :{}", filename);
            }
            reloader.reload(filename, content);
        } catch (Throwable throwable) {
            logger.error("read file error path:{}", filename, throwable);
        }
    }
}
