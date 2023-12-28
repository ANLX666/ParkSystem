package com.imust.controller;

import com.imust.entity.*;
import com.imust.service.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private  NoticeService noticeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/login")
    public String login(Admin admins, HttpSession session, Model model){
        Admin admin=adminService.login(admins);
        if(admin!=null){
            session.setAttribute("LogAdmin",admin);
          return "/admin/index";
        }
        else{
            model.addAttribute("msg","账号或密码错误");
            return "/admin/login";
        }

    }

    @RequestMapping("/logout")
    public String Logout(HttpSession session){
        session.removeAttribute("LogAdmin");
        return "admin/login";
    }

    @RequestMapping("/admin-add")
    public String registerAdmin(){
        return "/admin/admin-add";
    }

    @RequestMapping("/admin-save")
    @ResponseBody
    public Map adminSave(String name, String password, String password2) {
        Admin admin = new Admin();
        admin.setName(name);
        if (password2.equals(password)) {
            admin.setPassword(password);
        }
        List<Admin> adminByName = adminService.getAdminByName(name);
        int flag = 0;
        boolean isSuccess = false;
        if(adminByName.isEmpty()) {
            flag = 1;
        }else{
            isSuccess = false;
        }
        if (flag == 1){
            isSuccess = adminService.addAdmin(admin);
        }
        // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", -1);
        }
        return responseData;
    }

    @RequestMapping("/admin-list")
    public String adminList(HttpSession session,Model model){
        // Admin logAdmin =(Admin) session.getAttribute("LogAdmin");
        // List<Admin> adminByName = adminService.getAdminByName(logAdmin.getName());
        // model.addAttribute("adminList",adminByName);
        List<Admin> allAdmin = adminService.getAllAdmin();
        model.addAttribute("adminList",allAdmin);
        model.addAttribute("adminNum",allAdmin.size());
        return "/admin/admin-list";
    }

    @RequestMapping("/change-info")
    public String toAdminInfo(Admin admin, Model model){
        // adminService.updateAdmin(admin);
        // Admin admin1=adminService.getAdminById(admin.getId());
        // model.addAttribute("admin",admin1);
        return "/admin/change-info";
    }

    @RequestMapping("/updateAdmin")
    public String updateAdmin(HttpSession session,String name,String password,String password2,Model model){
        Admin admin1 = new Admin();
        Admin logAdmin = (Admin) session.getAttribute("LogAdmin");
        admin1.setId(logAdmin.getId());
        admin1.setName(name);
        if(password2.equals(password)){
            admin1.setPassword(password);
        }
        boolean b = adminService.updateAdmin(admin1);
        System.out.println(admin1.getPassword());
        if(b){
            return "redirect:/admin/logout";
        }
        return "admin/change-info";
    }

    @RequestMapping("/delAdmin")
    @ResponseBody
    // 在服务器端修改为通过@RequestParam注解获取请求参数id
    public Map delAdmin(@RequestParam("adminId") int adminId,Model model){
        // Admin adminById = adminService.getAdminById(id);
        boolean isSuccess = adminService.delAdmin(adminId);
        // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", -1);
        }
        List<Admin> allAdmin = adminService.getAllAdmin();
        model.addAttribute("adminNum",allAdmin.size());
        return responseData;
    }

    @RequestMapping("/findAdminByName")
    //@RequestParam注解来获取用户输入的name
    public String findAdminByName(@RequestParam("nameTmp") String name, Model model) {
        List<Admin> adminList = adminService.getAdminByName(name);
        model.addAttribute("adminList", adminList);
        model.addAttribute("adminNum",adminList.size());
        return "/admin/admin-list";
    }

    @RequestMapping("/message-list")
    public String messageList(Model model){
        List<Message> all = messageService.getAll();
        model.addAttribute("messageList",all);
        model.addAttribute("messageNum",all.size());
        return "message/message-list";
    }

    @RequestMapping("findMessageByContent")
    //@RequestParam注解来获取用户输入的留言
    public String findMessageByContent(@RequestParam("contentTmp") String content, Model model) {
        List<Message> messageContent = messageService.getByContent(content);
        model.addAttribute("messageList", messageContent);
        model.addAttribute("messageNum",messageContent.size());
        return "message/message-list";
    }

    @RequestMapping("/delMessage")
    @ResponseBody
    public Map delMessage(@RequestParam("messageId") int messageId,Model model){
        boolean isSuccess = messageService.delMessage(messageId);
        // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        List<Message> allMessage = messageService.getAll();
        model.addAttribute("messageNum",allMessage.size());
        return responseData;
    }

    @RequestMapping("/answer-add")
    public String addAnswer(Model model, int messageId){
        model.addAttribute("messageId" ,messageId );
        return "message/answer-add";
    }

    @RequestMapping("/answer-save")
    @ResponseBody
    public Map saveAnswer(@RequestParam("id") int id,String answer,HttpSession session,Model model){
        Message message = messageService.getByid(id);
        message.setAnswer(answer);
        Admin admin = (Admin) session.getAttribute("LogAdmin");
        message.setAdmin_id(admin.getId());
        message.setAdmin_name(admin.getName());
        boolean isSuccess = messageService.updateMessage(message);
        // // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        return responseData;
    }

    @RequestMapping("/user-list")
    public String userList(Model model){
        List<Users> allUser =userService.getAll();
        model.addAttribute("userList",allUser);
        model.addAttribute("userNum",allUser.size());
        return "user/user-list";
    }

    @RequestMapping("/findUserByName")
    public String findUserByName(@RequestParam("nameTmp") String name, Model model) {
        List<Users> userName = userService.getPointByName(name);
        model.addAttribute("userList", userName);
        model.addAttribute("userNum",userName.size());
        return "user/user-list";
    }

    @RequestMapping("/car-list")
    public String carList(Model model){
        List<Park> allCar =parkService.getAll();
        model.addAttribute("carList",allCar);
        model.addAttribute("carNum",allCar.size());
        return "car/car-list";
    }

    @RequestMapping("/findCarByKey")
    public String findCarByKey(@RequestParam("keyTmp") String key, Model model) {
        List<Park> carKey = parkService.getByKey(key);
        model.addAttribute("carList", carKey);
        model.addAttribute("carNum",carKey.size());
        return "car/car-list";
    }

    @RequestMapping("/delCar")
    @ResponseBody
    public Map delCar(@RequestParam("carId") int carId,Model model){
        boolean isSuccess = parkService.delCar(carId);
        // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        List<Park> allCar = parkService.getAll();
        model.addAttribute("carNum",allCar.size());
        return responseData;
    }

    @RequestMapping("/car-edit")
    public String toEdit(int carId, Model model) {
        Park car = parkService.getById(carId);
        List<DiQu> diQuList = parkService.getDiQuList();
        model.addAttribute("diquList",diQuList);
        model.addAttribute("car", car);
        return "car/car-edit";
    }

    @RequestMapping("/car-update")
    @ResponseBody
    public Map carEdit(Park park){
        boolean b = parkService.updateCar(park);
        Map<String, Object> responseData = new HashMap<>();
        if (b) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        return responseData;
    }

    @RequestMapping("/car-add")
    public String toCarAdd(Model model){
        List<DiQu> diQuList = parkService.getDiQuList();
        model.addAttribute("diquList",diQuList);
        return "car/car-add";
    }

    @RequestMapping("/car-save")
    @ResponseBody
    public Map carAdd(Park park){
        boolean b = parkService.addCar(park);
        Map<String, Object> responseData = new HashMap<>();
        if (b) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        return responseData;
    }

    @RequestMapping("/order-list")
    public String orderList(Model model){
        List<Order> orderList = orderService.getAll();
        model.addAttribute("orderList",orderList);
        model.addAttribute("orderNum",orderList.size());
        return "order/order-list";
    }

    @RequestMapping("/delOrder")
    @ResponseBody
    public Map delOrder(@RequestParam("orderId") int orderId,Model model){
        boolean isSuccess = orderService.delOrder(orderId);
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        List<Order> allOrder = orderService.getAll();
        model.addAttribute("orderNum",allOrder.size());
        return responseData;
    }

    @RequestMapping("/findOrderByKey")
    public String findOrderByKey(@RequestParam("keyTmp") String key,Model model){
        List<Order> orderByKey = orderService.getByKey(key);
        model.addAttribute("orderList",orderByKey);
        model.addAttribute("orderNum",orderByKey.size());
        return "order/order-list";
    }

//    @RequestMapping("/point-list")

    @RequestMapping("/point-list")
    public String pointList(Model model){
        List<Users> allPoint = userService.getAllPoint();
        model.addAttribute("pointList",allPoint);
        model.addAttribute("pointNum",allPoint.size());
        return "point/point-list";
    }

    @RequestMapping("findPointByName")
    public String findPointByName(@RequestParam("nameTmp") String name,Model model){
        List<Users> pointByName = userService.getPointByName(name);
        model.addAttribute("pointList",pointByName);
        model.addAttribute("pointNum",pointByName.size());
        return "point/point-list";
    }

    @RequestMapping("/vipList")
    public String vipList(Model model){
        List<Users> users = userService.queryVipList();
        model.addAttribute("vipList",users);
        return "user/vip-list";
    }

    @RequestMapping("/sartVip")
    @ResponseBody
    public Map startVip(@RequestParam("userId") int userId,Users user,Model model){
//        boolean isSuccess = userService.sartVip(userId,user.getStauts());
        Map<String, Object> responseData = new HashMap<>();
        if (userService.sartVip(userId,1)){
            List<VipShenQing> vipShenQings = userService.queryVipListByUserId(userId);
//            System.out.println(vipShenQings.size());
            for (int i = 0; i<vipShenQings.size() ; i++) {
                userService.deleteShenPiId(vipShenQings.get(i).getId());
//                System.out.println(vipShenQings.get(i).getId());
            }
            responseData.put("res", 0);
        }
//        if (isSuccess) {
//            responseData.put("res", 0);
//        } else {
//            responseData.put("res", 1);
//        }
        List<Users> users = userService.queryVipList();
        model.addAttribute("vipList",users);
        return responseData;
    }

    @RequestMapping("/stopVip")
    @ResponseBody
    public Map stopVip(@RequestParam("userId") int userId,Users user,Model model){
        boolean isSuccess = userService.stopVip(userId,user.getStauts());
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        List<Users> users = userService.queryVipList();
        model.addAttribute("vipList",users);
        return responseData;
    }

    @RequestMapping("/notice-list")
    public String noticeList(Model model){
        List<Notice> allList = noticeService.getAll();
        model.addAttribute("noticeNum",allList.size());
        model.addAttribute("noticeList",allList);
        return "notice/notice-list";
    }

    @RequestMapping("/findNoticeByTitle")
    public String findNoticeByTitle(@RequestParam("titleTmp") String title, Model model) {
        List<Notice> titleList = noticeService.getByTitle(title);
        model.addAttribute("noticeNum",titleList.size());
        model.addAttribute("noticeList",titleList);
        return "notice/notice-list";
    }

    @RequestMapping("/delNotice")
    @ResponseBody
    public Map delNotice(@RequestParam("noticeId") int noticeId,Model model){
        boolean isSuccess = noticeService.delNotice(noticeId);
        // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        List<Notice> allList = noticeService.getAll();
        model.addAttribute("noticeNum",allList.size());
        return responseData;
    }

    @RequestMapping("/notice-edit")
    public String toNoticeEdit(int noticeId, Model model) {
        Notice byId = noticeService.getById(noticeId);
        model.addAttribute("notice", byId);
        return "notice/notice-edit";
    }

    @RequestMapping("/notice-update")
    @ResponseBody
    public Map noticeEdit(Notice notice){
        boolean b = noticeService.updateNotice(notice);
        Map<String, Object> responseData = new HashMap<>();
        if (b) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        return responseData;
    }

    @RequestMapping("/notice-add")
    public String toNoticeadd(){
        return "notice/notice-add";
    }

    @RequestMapping("/notice-save")
    @ResponseBody
    public Map noticeAdd(Notice notice,HttpSession session){
        Admin logAdmin = (Admin)session.getAttribute("LogAdmin");
        notice.setAdmin_id(logAdmin.getId());
        notice.setAdmin_name(logAdmin.getName());
        boolean b = noticeService.addNotice(notice);
        Map<String, Object> responseData = new HashMap<>();
        if (b) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", 1);
        }
        return responseData;
    }


}
