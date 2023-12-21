package com.imust.controller;

import com.imust.entity.DiQu;
import com.imust.entity.Park;
import com.imust.service.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/car")
public class ParkController {
    // 查询车位
    @Autowired
    private ParkService parkService;
    @RequestMapping("car-select")
    public String selectCarList(Model model){
        List<Park> all = parkService.getAll();
        System.out.println(all);
        // 查询地区
        List<DiQu> diQuList = parkService.getDiQuList();
        model.addAttribute("diquList" , diQuList);
        model.addAttribute("carList" , all);
        return "list";
    }
    @RequestMapping("detail")
    public String carDetail(int id , Model model){
        Park park = parkService.getById(id);
        model.addAttribute("car",park);
        return "detail";
    }

//    @RequestMapping("buy")
//    public String buyCar(int id , Model model){
//        boolean b = parkService.delCar(id);
//        return "detail";
//    }

    //根据地区id获取当前地区的车位信息
    @RequestMapping("/findCar")
    public String findParkByDqid(String diquId, Model model) {
        //如果点击全部，那么我们就查询所有
        if ("0".equals(diquId)) {
            List<Park> parkList = parkService.getAll();
            model.addAttribute("carList", parkList);
        } else {
            List<Park> allByKey = parkService.getAllByKey(diquId);
            model.addAttribute("carList", allByKey);
        }
        //将地区的列表重新查出来，再扔到页面上
        List<DiQu> diQuList = parkService.getDiQuList();
        model.addAttribute("diquList", diQuList);
        model.addAttribute("dqid",diquId);
        return "list";
    }
}
