package network.vena.cooperation.websocket.controller;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import network.vena.cooperation.websocket.entity.UserChatRecord;
import network.vena.cooperation.websocket.service.IUserChatRecordService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/message/chatRecord")
@Slf4j
public class MallUserChatRecordController {
	@Autowired
	private IUserChatRecordService mallUserChatRecordService;

	@GetMapping(value = "/list")
	public Result<?> queryPageList(UserChatRecord mallUserChatRecord,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<UserChatRecord> queryWrapper = QueryGenerator.initQueryWrapper(mallUserChatRecord,
				req.getParameterMap());
		Page<UserChatRecord> page = new Page<UserChatRecord>(pageNo, pageSize);
		IPage<UserChatRecord> pageList = mallUserChatRecordService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
}
