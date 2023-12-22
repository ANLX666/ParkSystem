package com.imust.controller;

import com.imust.entity.Message;
import com.imust.entity.Users;
import com.imust.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @RequestMapping("myMessage")
    public String getMessageList( Model model,  int id){
        List<Message> myMessage = messageService.getMyMessage(id);
        model.addAttribute("messageList" , myMessage);
        return "myMessage";
    }

//    @RequestMapping("message-save")
//    public String messageSave(){
//
//        return "myMessage";
//    }
    @RequestMapping("message-save")
    public String messageSave(String content , HttpSession httpSession){
        Message message = new Message();
        Date date_time = new Date();
        System.out.println(date_time);
        String pattern = "yyyy-MM-dd";
        Users logUser = (Users) httpSession.getAttribute("LogUser");
        // 创建 SimpleDateFormat 对象，将日期按指定格式转换为字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        // 将 Date 对象格式化为字符串
        String formattedDate = dateFormat.format(date_time);
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();  // 处理解析异常
        }
        message.setUser_id(logUser.getId());
        message.setCreateDate(parsedDate);
        messageService.addMessage(message);
        return "redirect:/message/myMessage?id=" + logUser.getId();
    }
    @RequestMapping("delMsg")
    public String delMsg(int id , HttpSession httpSession){
        Users logUser = (Users) httpSession.getAttribute("LogUser");
        messageService.delMessage(id);
        return "redirect:/message/myMessage?id="+ logUser.getId();
    }
}
