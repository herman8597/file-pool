package network.vena.cooperation.websocket.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import network.vena.cooperation.websocket.entity.UserChatRecord;
import network.vena.cooperation.websocket.mapper.MallUserChatRecordMapper;
import network.vena.cooperation.websocket.service.IUserChatRecordService;

@Service
public class UserChatRecordService extends ServiceImpl<MallUserChatRecordMapper, UserChatRecord> implements IUserChatRecordService {

}
