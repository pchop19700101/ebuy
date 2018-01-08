package cn.porkchop.ebuy.content.service.impl;

import cn.porkchop.ebuy.content.service.ContentCategoryService;
import cn.porkchop.ebuy.mapper.TbContentCategoryMapper;
import cn.porkchop.ebuy.pojo.E3Result;
import cn.porkchop.ebuy.pojo.EasyUITreeNode;
import cn.porkchop.ebuy.pojo.TbContentCategory;
import cn.porkchop.ebuy.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //封装为list返回
        ArrayList<EasyUITreeNode> easyUITreeNodeList = new ArrayList<>();
        for(TbContentCategory tbContentCategory:tbContentCategoryList){
            easyUITreeNodeList.add(new EasyUITreeNode(tbContentCategory.getId(),tbContentCategory.getName(),tbContentCategory.getIsParent()?"closed":"open"));
        }
        return easyUITreeNodeList;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setIsParent(false);
        //状态。可选值:1(正常),2(删除)
        tbContentCategory.setStatus(1);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        tbContentCategory.setSortOrder(1);
        tbContentCategoryMapper.insert(tbContentCategory);
        //判断父节点的isparent的值是否正确
        TbContentCategory parentTbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parentTbContentCategory.getIsParent()){
            parentTbContentCategory.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parentTbContentCategory);
        }
        return E3Result.ok(tbContentCategory);
    }
}
