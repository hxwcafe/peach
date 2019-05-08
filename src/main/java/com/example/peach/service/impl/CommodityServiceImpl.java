package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.CommodityMapper;
import com.example.peach.pojo.Commodity;
import com.example.peach.service.CommodityService;
import com.example.peach.service.OrderPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Resource
    private CommodityMapper commodityMapper;
    @Resource
    private OrderPayService orderPayService;

    /**
     * 显示所有会员卡信息
     *
     * @return
     */
    @Override
    public ServiceResponse<List> selectCommodity() {
        List<Commodity> list = commodityMapper.selectCommodity();
        if (list != null) {
            return ServiceResponse.createBySuccess(list);
        }
        return ServiceResponse.createByError("查询错误", list);

    }

    /**
     * 添加商品
     *
     * @param record 商品实体类
     * @return
     */
    @Override
    public ServiceResponse<String> insertSelective(Commodity record) {
        if (record != null) {
            int getrow = commodityMapper.insert(record);
            if (getrow > 0) {
                return ServiceResponse.createBySuccess("添加成功");
            }
            return ServiceResponse.createByError("商品添加失败");
        } else {
            return ServiceResponse.createByError("信息为空");
        }
    }

    /**
     * 修改vip会员卡信息
     *
     * @param commodity 商品实体类
     * @return true false
     */
    @Override
    public ServiceResponse<String> updateCommodity(Commodity commodity) {
        if (commodity != null) {
            int getrows = commodityMapper.updateByPrimaryKeySelective(commodity);
            if (getrows > 0) {
                ServiceResponse.createBySuccess("修改成功");
            } else {
                ServiceResponse.createByError("修改失败");
            }
        }
        return ServiceResponse.createByError("信息为空");
    }

    /**
     * 删除vip卡商品
     *
     * @param id vip卡id
     * @return
     */
    @Override
    public ServiceResponse<String> delCommodity(Integer id) {
        int getrows = commodityMapper.deleteByPrimaryKey(id);
        if (getrows > 0) {
            return ServiceResponse.createBySuccess("删除成功");
        } else {
            return ServiceResponse.createByError("删除失败");
        }
    }


}
