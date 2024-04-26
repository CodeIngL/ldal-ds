package com.codeL.data.ds.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TxService {


    @Autowired
    private DemoService demoService;

    /**
     * 普通事物
     */
    @Transactional
    public void insert() {
        demoService.insert();
        demoService.insert2();
    }


    /**
     * 嵌套事物
     */
    @Transactional
    public void insert2() {
        demoService.insert();
        demoService.insert3();
    }

}
