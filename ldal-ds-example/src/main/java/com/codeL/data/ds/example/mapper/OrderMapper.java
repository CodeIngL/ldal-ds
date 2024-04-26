package com.codeL.data.ds.example.mapper;

//import com.llpay.framework.autoconfigure.jdbc.tkmapper.Mapper;

import com.codeL.data.ds.example.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fengsihang
 * @date 2018/7/13
 */
@Mapper
@Repository
public interface OrderMapper {

    /**
     * insert
     *
     * @param order
     */
    void insert(Order order);

    /**
     * userID
     *
     * @param id
     * @return
     */
    List<Order> findByUserId(Integer id);

    /**
     * orderID
     *
     * @param id
     * @return
     */
    List<Order> findByOrderId(Integer id);

}
