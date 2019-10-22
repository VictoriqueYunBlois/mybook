package com.llk.book.controller;

import com.llk.book.annotation.LoginRequired;
import com.llk.book.common.DataGridDataSource;
import com.llk.book.common.JsonData;
import com.llk.book.common.PageBean;
import com.llk.book.model.BookInfo;
import com.llk.book.model.BookType;
import com.llk.book.service.BookInfoService;
import com.llk.book.service.BookTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookinfo")
public class BookInfoController {

    @Resource
    private BookInfoService bookInfoService;

    @Resource
    private BookTypeService bookTypeService;


    /**
     * 新增图书
     */
    @PostMapping("/save")
    @LoginRequired
    public JsonData saveBookInfo(BookInfo bookInfo) {
        int count = bookInfoService.saveBookInfo(bookInfo);
        if (count > 0) {
            return JsonData.success(count, "新增成功");
        } else {
            return JsonData.fail("新增失败");
        }
    }


    /**
     * 编辑图书
     */
    @PutMapping("/update")
    @LoginRequired
    public JsonData updateBookInfo(BookInfo bookInfo) {
        int count = bookInfoService.updateBookInfo(bookInfo);
        if (count > 0) {
            return JsonData.success(count, "编辑成功");
        } else {
            return JsonData.fail("编辑失败");
        }
    }

    /**
     * 根据id删除图书
     */
    @DeleteMapping("/delete")
    @LoginRequired
    public JsonData deleteBookInfo(Integer bookId) {
        int count = bookInfoService.deleteBookInfo(bookId);
        if (count > 0) {
            return JsonData.success(count, "删除成功");
        } else {
            return JsonData.fail("删除失败");
        }
    }


    /**
     * 图书列表
     */
    @PostMapping("/list")
    @LoginRequired
    public DataGridDataSource<BookInfo> bookInfoList(@RequestParam(value = "bookIsbn", required = false, defaultValue = "") String bookIsbn,
                                                     @RequestParam(value = "bookName", required = false, defaultValue = "") String bookName,
                                                     @RequestParam(value = "bookAuthor", required = false, defaultValue = "") String bookAuthor,
                                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                     @RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) {
        PageBean pageBean = new PageBean(page, rows);
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("bookIsbn", "%" + bookIsbn + "%");
        map.put("bookName", "%" + bookName + "%");
        map.put("bookAuthor", "%" + bookAuthor + "%");
        List<BookInfo> bookInfoList = bookInfoService.selectBookInfoList(map);

        for (BookInfo bookInfo : bookInfoList) {
            List<BookType> bookTypeList = bookTypeService.selectBookTypeListByBookTypeId(bookInfo.getBookType());
            for (BookType bookType : bookTypeList) {
                bookInfo.setTypes(bookType.getBookTypeName());
            }
        }
        int totalBook = bookInfoService.getTotalBook(map);
        DataGridDataSource<BookInfo> bookInfoDataGridDataSource = new DataGridDataSource<>();
        bookInfoDataGridDataSource.setTotal(totalBook);
        bookInfoDataGridDataSource.setRows(bookInfoList);
        return bookInfoDataGridDataSource;
    }


    /**
     * 图书详情
     */
    @GetMapping("/detail")
    @LoginRequired
    public JsonData bookInfoDetail(Integer bookId) {
        List<BookInfo> bookInfos = bookInfoService.selectBookById(bookId);
        for (BookInfo bookInfo : bookInfos) {
            List<BookType> bookTypeList = bookTypeService.selectBookTypeListByBookTypeId(bookInfo.getBookType());
            for (BookType bookType : bookTypeList) {
                bookInfo.setTypes(bookType.getBookTypeName());
            }
        }
        return JsonData.success(bookInfos);
    }


    /**
     * 图书信息(借书管理)
     */
    @PostMapping("/info")
    @LoginRequired
    public JsonData bookInfo(Integer bookId) {
        return JsonData.success(bookInfoService.selectBookInfoById(bookId));
    }
}
