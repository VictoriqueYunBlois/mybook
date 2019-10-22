package com.llk.book.service;

import com.llk.book.model.BookType;

import java.util.List;

/**
 * BookTypeService
 */
public interface BookTypeService {

    /**
     * 查询所有图书分类信息
     */
    List<BookType> queryAllBookType();

    /**
     * 通过id查询图书分类信息
     */
    List<BookType> selectBookTypeListByBookTypeId(Integer bookTypeId);


    /**
     * 新增分类
     */
    int saveBookType(BookType bookType);


    /**
     * 修改分类
     */
    int updateBookType(BookType bookType);

    /**
     * 删除分类
     */
    int deleteBookType(Integer bookTypeId);


}