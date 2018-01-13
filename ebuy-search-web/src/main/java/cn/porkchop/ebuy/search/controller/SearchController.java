package cn.porkchop.ebuy.search.controller;

import cn.porkchop.ebuy.pojo.SearchResult;
import cn.porkchop.ebuy.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    private SearchItemService searchItemService;
    @Value("${SEARCH_RESULT_COUNT}")
    private int SEARCH_RESULT_COUNT;

    /**
     * 查询
     * @date 2018/1/13 13:02
     * @author porkchop
     */
    @RequestMapping("/search")
    private String search(String keyword , @RequestParam(defaultValue = "1") int page, Model model) throws Exception {
        //转码
        keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
        SearchResult searchResult = searchItemService.search(keyword, page, SEARCH_RESULT_COUNT);
        model.addAttribute("query",keyword);
        model.addAttribute("totalPages",searchResult.getTotalPage());
        model.addAttribute("recordCount",searchResult.getRecordCount());
        model.addAttribute("page",page);
        model.addAttribute("itemList",searchResult.getSearchItemList());
        return "search";
    }
}
