package com.codeL.data.ds.example.service;

import com.codeL.data.ds.example.entity.Order;
import com.codeL.data.ds.example.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {

    private static int a = 0;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void insert() {
        Order order = new Order();
        order.setOrderId(a);
        order.setUserId(a);
        a++;
        orderMapper.insert(order);
    }

    @Transactional
    public void insert2() {
        Order order = new Order();
        order.setOrderId(a);
        order.setUserId(a);
        a++;
        orderMapper.insert(order);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insert3() {
        Order order = new Order();
        order.setOrderId(a);
        order.setUserId(a);
        a++;
        orderMapper.insert(order);
    }


}
