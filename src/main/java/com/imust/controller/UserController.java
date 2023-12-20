package com.imust.controller;

import com.imust.entity.Users;
import com.imust.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //登录验证业务
    @RequestMapping("login")
    public String login(Users users, HttpSession session,
                        Model model){
        Users login = userService.login(users);
        if (login != null){
            if (users.getStauts() == 0){
                //可用id
                session.setAttribute("LogUser",login);
                System.out.println(login.getId());
                return "redirect:/index";
            }
            model.addAttribute("msg","该用户已经停权");
        }
        model.addAttribute("msg","用户名密码错误");
        return "join";
    }
    /**
     * 退出登录业务
     */
    @RequestMapping("logout")
    public String logout( HttpSession session
                         ){
        session.removeAttribute("LogUser");
        return "join";
    }

    @RequestMapping("user-save")
    public String register(String name,
                           String phone,
                           String plate_num,
                           String password){
        Users users = new Users();
        users.setName(name);
        users.setPassword(password);
        users.setPhone(phone);
        users.setPlate_num(plate_num);
        System.out.println(users.getPassword());
        List<Users> byName = userService.getByName(name);
        System.out.println(byName);
        if (byName.size() == 0) {
            System.out.println("2121");
            boolean b = userService.addUser(users);
            if (b == true){
                return "register-ok";
            }
            return  "register-fail";
        }
        return  "register-chongfu";
    }


    @RequestMapping("user-show")
    public String toUserInfo(int id, Model model){
        Users userById = userService.getUserById(id);
        System.out.println(userById.getName());
        model.addAttribute("userinfo",userById);
        return "user-show";
    }
    @RequestMapping("update-user")
    public String updateUser(Users users ,  Model model){
        boolean b = userService.updateUser(users);
        Users user = userService.getUserById(users.getId());
        model.addAttribute("userinfo",user);
//        if (b){
//            return "update-success";
//        }
//        return "update-error";
        return "user-show";
    }
    @RequestMapping("password")
    public String toPassword(){
        return "password";
    }
    @RequestMapping("updatePwd")
    public String updatePwd(int id , String oldpassword , String newpassword, Model model) {
        Users user = userService.getUserById(id);
        if (oldpassword.equals(user.getPassword())) {
            user.setPassword(newpassword);
            boolean b = userService.updateUserPwd(user);
            if (b) {
                model.addAttribute("usermsg" , "修改密码成功");
                System.out.println("修改密码成功");
                return "redirect:/user/logout";
            }
        }else {
            model.addAttribute("usermsg" , "old密码错误");
            System.out.println("密码错误");
        }
        return "password";
    }
}
