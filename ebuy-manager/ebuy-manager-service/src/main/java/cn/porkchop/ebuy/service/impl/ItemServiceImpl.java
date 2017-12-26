package cn.porkchop.ebuy.service.impl;

import cn.porkchop.ebuy.mapper.TbItemMapper;
import cn.porkchop.ebuy.pojo.EasyUIDataGridResult;
import cn.porkchop.ebuy.pojo.EasyUITreeNode;
import cn.porkchop.ebuy.pojo.TbItem;
import cn.porkchop.ebuy.pojo.TbItemExample;
import cn.porkchop.ebuy.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public TbItem getItemById(Long id) {
        return tbItemMapper.selectByPrimaryKey(id);

    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        PageHelper.startPage(page,rows);
        List<TbItem> list = tbItemMapper.selectByExample(new TbItemExample());
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        return new EasyUIDataGridResult(pageInfo.getTotal(),list);
    }

}
