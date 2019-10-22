package com.llk.book.service;

import com.llk.book.model.LendReturnList;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * LendBookService
 */
public interface LendBookService {

    /**
     * 借书
     */
    int lendBook(LendReturnList lendReturnList) throws ParseException;


    /**
     * 根据用户ID查询借还记录
     */
    List<LendReturnList> selectLendReturnRecordByUserId(Map<String, Object> map) throws ParseException;

    /**
     * 根据用户ID查询借还记录总数
     */
    int getTotalRecord(Map<String, Object> map);
}