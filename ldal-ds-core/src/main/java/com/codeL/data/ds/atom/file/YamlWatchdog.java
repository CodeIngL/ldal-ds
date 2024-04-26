package com.codeL.data.ds.atom.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class YamlWatchdog extends FileWatchdog {

    private static final Logger logger = LoggerFactory.getLogger(YamlWatchdog.class);

    private FileReloader reloader;

    public YamlWatchdog(String filename, FileReloader reloader) {
        super(filename);
        this.reloader = reloader;
    }

    public void doOnChange() {
        try {
            File file = new File(filename);
            String content = FileUtils.readFileToString(file, "UTF-8");
            reloader.reload(filename, content);
        } catch (Throwable throwable) {
            logger.error("read file error path:{}", filename, throwable);
        }
    }
}
