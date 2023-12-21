package com.imust.controller;

import com.imust.entity.Order;
import com.imust.entity.Users;
import com.imust.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("order")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @RequestMapping("showOrder")
    public String toOrder(Model model , HttpSession session){
        Users logUser = (Users) session.getAttribute("LogUser");
        List<Order> byId = orderService.getByUserId(logUser.getId());
        model.addAttribute("orderList" , byId) ;
        return "orderList";
    }
    @RequestMapping("jiesuan")
    public String jiesuan(int id){
        boolean b = orderService.delOrder(id);
        return "orderList";
    }
}
