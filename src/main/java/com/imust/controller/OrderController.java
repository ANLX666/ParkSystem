package com.imust.controller;

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

@RequestMapping("order")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ParkService parkService;
    @Autowired
    private UserService userService;
    @RequestMapping("showOrder")
    public String toOrder(Model model , HttpSession session){
        Users logUser = (Users) session.getAttribute("LogUser");
        List<Order> byId = orderService.getByUserId(logUser.getId());
        model.addAttribute("orderList" , byId) ;
        return "orderList";
    }
    @RequestMapping("jiesuan")
    public String jiesuan(int id){
        Order order = orderService.getById(id);
        order.setStatus(1);
        if (orderService.updateOrderStatus(order)){
            Park park = parkService.getById(order.getPark_id());
            park.setStatus(0);
            parkService.updateCar(park);
            Users users = userService.getUserById(order.getUser_id());
            users.setPoint(users.getPoint()+10);
            userService.updatePoint(users);
        }
        return "redirect:/order/showOrder";
    }
    @RequestMapping("delete")
    public String delete(int id){
        orderService.delOrder(id);
        return "redirect:/order/showOrder";
    }
}
