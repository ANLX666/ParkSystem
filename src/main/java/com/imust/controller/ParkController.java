package com.imust.controller;

import com.imust.entity.DiQu;
import com.imust.entity.Order;
import com.imust.entity.Park;
import com.imust.entity.Users;
import com.imust.service.OrderService;
import com.imust.service.ParkService;
import com.imust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/car")
public class ParkController {
    // 查询车位
    @Autowired
    private ParkService parkService;
    @Autowired
    private OrderService orderService;

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

    @RequestMapping("buy")
    public String buyCar(int id , Model model , HttpSession httpSession){
        boolean b = parkService.delCar(id);
        Park park = parkService.getById(id);
        double price = park.getPrice();
        int status = park.getStatus();
        Users users = (Users) httpSession.getAttribute("LogUser");
        int id1 = users.getId();
        int vipStaut = users.getVipStaut();
        if (vipStaut ==1 ){
            park.setStatus(1);
            int point = users.getPoint();
            if (parkService.updateCar(park)){
                Order order = new Order();
                order.setUser_id(id1);
                order.setPark_id(id);
                if (point >= 100 && point <= 300 ) {
                    order.setTotal(park.getPrice()*0.9);
                }
                else if (point>=300 && point <= 500 ){
                    order.setTotal(park.getPrice()*0.8);
                }else if (point >= 500 ) {
                    order.setTotal(park.getPrice()*0.7);
                }else {
                    order.setTotal(park.getPrice());
                }
                orderService.addOrder(order);
                return "redirect:/order/showOrder";
            }
        }else {
            return "notVip";
        }
        return "detail";
    }

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
