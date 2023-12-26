package com.imust.controller;

import com.imust.entity.*;
import com.imust.service.AdminService;
import com.imust.service.MessageService;
import com.imust.service.ParkService;
import com.imust.service.UserService;
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
    private MessageService messageService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private UserService userService;

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





}
