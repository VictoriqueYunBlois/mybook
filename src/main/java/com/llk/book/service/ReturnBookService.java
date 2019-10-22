package com.llk.book.service;

import com.llk.book.model.LendReturnList;

import java.text.ParseException;
import java.util.List;

/**
 * ReturnBookService
 */
public interface ReturnBookService {

    /**
     * 根据图书id查询图书及借阅者信息
     */
    List<LendReturnList> selectBookInfoAndUserByBookId(Integer bookId) throws ParseException;


    /**
     * 还书
     */
    int returnBook(LendReturnList lendReturnList) throws ParseException;
}
