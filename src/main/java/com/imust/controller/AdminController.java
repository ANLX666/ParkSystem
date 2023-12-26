package com.imust.controller;




import com.imust.entity.Admin;
import com.imust.entity.Message;
import com.imust.entity.Park;
import com.imust.service.AdminService;
import com.imust.service.MessageService;
import com.imust.service.ParkService;
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
    @RequestMapping("/login")
    public String login(Admin admins, HttpSession session, Model model){
        Admin admin=adminService.login(admins);
        if(admin!=null){
            session.setAttribute("LogAdmin",admin);
          return "/admin/index";
        }
        else{
            model.addAttribute("msg","账号或密码错误");
        }
        return "/admin/login";
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
    public ResponseEntity<Object> adminSave(String name, String password, String password2) {
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
        return new ResponseEntity<>(responseData, HttpStatus.OK);
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
    // 在服务器端修改为通过@RequestParam注解获取请求参数id
    public ResponseEntity<Object> delAdmin(@RequestParam("adminId") int adminId,Model model){
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
        return new ResponseEntity<>(responseData, HttpStatus.OK);
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
    public ResponseEntity<Object> delMessage(@RequestParam("messageId") int messageId,Model model){
        boolean isSuccess = messageService.delMessage(messageId);
        // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", -1);
        }
        List<Message> allMessage = messageService.getAll();
        model.addAttribute("messageNum",allMessage.size());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @RequestMapping("/answer-add")
    public String addAnswer(Model model, int messageId){
        model.addAttribute("messageId" ,messageId );
        return "message/answer-add";
    }

    @RequestMapping("/answer-save")
    public ResponseEntity<Object>  saveAnswer(@RequestParam("id") int id,String answer,HttpSession session,Model model){
        System.out.println(1111);
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
            responseData.put("res", -1);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
        // System.out.println(answer);
        // return "/message/message-list";
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
    public ResponseEntity<Object> delCar(@RequestParam("carId") int carId,Model model){
        boolean isSuccess = parkService.delCar(carId);
        // 封装返回数据格式
        Map<String, Object> responseData = new HashMap<>();
        if (isSuccess) {
            responseData.put("res", 0);
        } else {
            responseData.put("res", -1);
        }
        List<Park> allCar = parkService.getAll();
        model.addAttribute("carNum",allCar.size());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @RequestMapping("/car-edit")
    public String toEdit(){
        return "car/car-edit";
    }

    @RequestMapping("/car-update")
    public String carEdit(){

        return null;
    }

    @RequestMapping("/car-add")
    public String toCarAdd(){
        return "car/car-add";
    }



}
