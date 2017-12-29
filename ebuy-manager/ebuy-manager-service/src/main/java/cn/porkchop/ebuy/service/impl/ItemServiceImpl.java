package cn.porkchop.ebuy.service.impl;

import cn.porkchop.ebuy.mapper.TbItemDescMapper;
import cn.porkchop.ebuy.mapper.TbItemMapper;
import cn.porkchop.ebuy.pojo.*;
import cn.porkchop.ebuy.service.ItemService;
import cn.porkchop.ebuy.utils.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

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

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        long id = IDUtils.genItemId();
        //补全不填写的属性
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //插入表中
        int insert = tbItemMapper.insert(tbItem);
        //创建详情
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(id);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemDesc(desc);
        //插入详情
        tbItemDescMapper.insert(tbItemDesc);
        return E3Result.ok();
    }

}
