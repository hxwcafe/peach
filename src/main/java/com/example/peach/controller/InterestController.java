package com.example.peach.controller;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Interest;
import com.example.peach.pojo.merge.InterestU;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.service.InterestService;
import com.example.peach.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by Administrator on 2018/12/4.
 */
@RestController
@RequestMapping(value = "/interest")
public class InterestController {

    @Resource
    private InterestService interestService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ServiceResponse selectById(@RequestParam Integer id) {
        if (id!=null){
            List<String> list = interestService.selectById(id);
            return ServiceResponse.createBySuccess(list);
        }else{
            return ServiceResponse.createByError("传入空值！");
        }
    }


    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public ServiceResponse selectUserByInterest(@RequestParam Integer id){

        if (id!=null){
            ServiceResponse response=interestService.selectUserByInterest(id);
            return response;
        }else {
            return ServiceResponse.createByError("传入空值！");
        }
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServiceResponse updateInterest(@RequestParam Integer id, @RequestParam(value = "data[]")String[] data){

        String str= StringUtils.join(data,",");
        Interest interest=new Interest();
        interest.setId(id);
        interest.setCategory(str);
        return interestService.updateInterest(interest);
    }


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ServiceResponse insertInerest(@ModelAttribute Interest interest){

        return interestService.insertInterest(interest);

    }

    /**
     *查询所有兴趣详细
     * @return
     */
    @RequestMapping(value = "/allcategory", method = RequestMethod.GET)
    public ServiceResponse<List<String>> selectAllInterest(){
        return interestService.selectAllInterest();
    }

    /**
     *  查询所有兴趣(兴趣,user数量)
     * @return list集合
     */
    @RequestMapping(value = "/allInteretU",method = RequestMethod.GET)
    public ServiceResponse<List<InterestU>> selectInteretU(){
        return interestService.selectInteretU();
    }

    /**
     * 根据兴趣id查询所有用户
     * @param id 兴趣id
     * @return
     */
    @RequestMapping(value = "/allUser",method = RequestMethod.GET)
    public ServiceResponse<Pager<UAlbum>> selectUserAlbumBy(Integer id, Integer pageIndex){
        return interestService.selectUserAlbumBy(id,pageIndex);
    }

}
