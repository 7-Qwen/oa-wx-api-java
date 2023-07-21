package com.wen.oawxapi.controller;

import com.wen.oawxapi.common.utils.JwtUtils;
import com.wen.oawxapi.common.utils.R;
import com.wen.oawxapi.service.MessageService;
import com.wen.oawxapi.vo.form.DeleteMessageByIdForm;
import com.wen.oawxapi.vo.form.MessageIdForm;
import com.wen.oawxapi.vo.form.MessagePageForm;
import com.wen.oawxapi.vo.form.UpdateMessageReadFlagForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author: 7wen
 * @Date: 2023-07-21 16:56
 * @description:
 */
@RestController
@CrossOrigin
@Api(tags = "消息api")
@RequestMapping(value = "/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private JwtUtils jwtUtils;

    @ApiOperation(value = "获取分页消息列表")
    @PostMapping(value = "/getMessageByPage")
    public R getMessageByPage(@Valid @RequestBody MessagePageForm messagePageForm, @RequestHeader("token") String token) {
        return R.ok().put("result", messageService.searchMessageByPage(jwtUtils.getUserId(token), messagePageForm.getPage(), messagePageForm.getSize()));
    }

    @ApiOperation(value = "根据消息id获取消息")
    @PostMapping(value = "/getMessageById")
    public R getMessageById(@Valid @RequestBody MessageIdForm messageIdForm) {
        return R.ok().put("result", messageService.searchMessageById(messageIdForm.getId()));
    }

    @ApiOperation(value = "未读变更已读")
    @PostMapping(value = "/updateReadFlag")
    public R updateReadFlag(@Valid @RequestBody UpdateMessageReadFlagForm updateMessageReadFlagForm) {
        return R.ok().put("result", messageService.updateUnreadMessage(updateMessageReadFlagForm.getId()) == 1);
    }

    @ApiOperation(value = "根据消息id删除消息")
    @PostMapping(value = "/deleteMessageById")
    public R deleteMessageById(@Valid @RequestBody DeleteMessageByIdForm deleteMessageByIdForm) {
        return R.ok().put("result", messageService.deleteMessageRefById(deleteMessageByIdForm.getId()) == 1);
    }

    @ApiOperation(value = "轮询接收消息")
    @GetMapping("/refreshMessage")
    public R refreshMessage(@RequestHeader("token") String token) {
        Long userId = jwtUtils.getUserId(token);
        //查询有多少新消息,有多少未读消息
        return R.ok().put("lastRows", messageService.searchLastCount(userId)).put("unRead", messageService.searchUnreadCount(userId));
    }
}
