package cn.porkchop.ebuy.manager.service.impl;

import cn.porkchop.ebuy.mapper.TbItemCatMapper;
import cn.porkchop.ebuy.pojo.EasyUITreeNode;
import cn.porkchop.ebuy.pojo.TbItemCat;
import cn.porkchop.ebuy.pojo.TbItemCatExample;
import cn.porkchop.ebuy.manager.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getCatList(long parentId) {
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = tbItemCatMapper.selectByExample(tbItemCatExample);
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat:list){
            resultList.add(new EasyUITreeNode(tbItemCat.getId(),tbItemCat.getName(),tbItemCat.getIsParent()?"closed":"open"));
        }
        return resultList;
    }
}
