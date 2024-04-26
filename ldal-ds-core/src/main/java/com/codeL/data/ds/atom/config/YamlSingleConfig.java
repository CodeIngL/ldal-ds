package com.codeL.data.ds.atom.config;

import com.codeL.data.ds.atom.yaml.constructor.YamlOrchNoneSingleConfigConstructor;
import com.codeL.data.ds.common.yaml.config.YamlConfiguration;
import com.codeL.data.ds.common.yaml.engine.YamlEngine;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class YamlSingleConfig implements YamlConfiguration {

    private DataSource dataSource;

    private Map<String, String> props;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public static void main(String[] args) {
        String contents = "" +
                "    dataSource:  !!com.alibaba.druid.pool.DruidDataSource\n" +
                "            url: jdbc:mysql://localhost:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n" +
                "    props: \n" +
                "            url: jdbc:mysql://localhost:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n" +
                "            username: root\n" +
                "            password: 123456\n" +
                "            initialSize: 5\n" +
                "            # 最小连接池数量\n" +
                "            minIdle: 10\n" +
                "            # 最大连接池数量\n" +
                "            maxActive: 20\n" +
                "            # 配置获取连接等待超时的时间\n" +
                "            maxWait: 60000\n" +
                "            # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\n" +
                "            timeBetweenEvictionRunsMillis: 60000\n" +
                "            # 配置一个连接在池中最小生存的时间，单位是毫秒\n" +
                "            minEvictableIdleTimeMillis: 300000\n" +
                "            # 配置一个连接在池中最大生存的时间，单位是毫秒\n" +
                "            maxEvictableIdleTimeMillis: 900000\n" +
                "            # 配置检测连接是否有效\n" +
                "            validationQuery: SELECT 1 FROM DUAL\n" +
                "            testWhileIdle: true\n" +
                "            testOnBorrow: false\n" +
                "            testOnReturn: false\n";
        try {
            YamlSingleConfig config = unmarshal(contents.getBytes());
            System.out.println(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlSingleConfig unmarshal(final File yamlFile) throws IOException {
        return YamlEngine.unmarshal(yamlFile, YamlSingleConfig.class, new YamlOrchNoneSingleConfigConstructor());
    }

    public static YamlSingleConfig unmarshal(final byte[] bytes) throws IOException {
        return YamlEngine.unmarshal(bytes, YamlSingleConfig.class, new YamlOrchNoneSingleConfigConstructor());
    }
}
