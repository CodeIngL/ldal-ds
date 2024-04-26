package com.codeL.data.ds.example.controller;

import com.codeL.data.ds.example.entity.Order;
import com.codeL.data.ds.example.mapper.OrderMapper;
import com.codeL.data.ds.example.service.TxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/demo")
public class DemoController {

    private static int a = 0;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    TxService txService;


    /**
     * 普通直接插入
     * @return
     */
    @RequestMapping("/insert")
    public String insert() {
        Order order = new Order();
        order.setOrderId(a);
        order.setUserId(a);
        a++;
        orderMapper.insert(order);
        return "OK";
    }

    /**
     * 普通读取
     * @return
     */
    @RequestMapping("/read")
    public List<Order> read(@RequestParam("abc") String abc) {
        return orderMapper.findByUserId(Integer.valueOf(abc));
    }


    /**
     * 事物插入
     * @return
     */
    @RequestMapping("/insertTx")
    public String insertTx() {
        txService.insert();
        return "OK";
    }

    /**
     * 内嵌事物插入
     * @return
     */
    @RequestMapping("/insertNestedTx")
    public String insertTx2() {
        txService.insert2();
        return "OK";
    }
}
