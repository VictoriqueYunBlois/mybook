package com.llk.book.service;

import com.llk.book.model.BookInfo;

import java.util.List;
import java.util.Map;

/**
 * BookInfoService
 */
public interface BookInfoService {


    /**
     * 新增图书
     */
    int saveBookInfo(BookInfo bookInfo);

    /**
     * 编辑图书
     */
    int updateBookInfo(BookInfo bookInfo);


    /**
     * 删除图书
     */
    int deleteBookInfo(Integer bookId);


    /**
     * 查询图书列表
     */
    List<BookInfo> selectBookInfoList(Map<String, Object> map);

    /**
     * 查询图书列表总数
     */
    int getTotalBook(Map<String, Object> map);

    /**
     * 根据id查询图书
     */
    List<BookInfo> selectBookById(Integer bookId);


    /**
     * 根据id查询图书
     */
    BookInfo selectBookInfoById(Integer bookId);

}
