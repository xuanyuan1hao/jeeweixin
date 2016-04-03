package com.wxapi.process;

/**
 * 消息类型：所有微信涉及到的消息类型统一管理
 */

public enum MsgType {

	Text("text"),//文本消息
	News("news"),//图文消息
	Location("location"),//地理位置消息
	Image("image"),//图片消息
	Voice("voice"),//语音消息
	Video("video"),//视频消息
	Event("event"),//事件消息
	Default("defalut"),//默认消息
	MPNEWS("mpnews"),//群发图文消息
	//自定义消息部分
	WaitCreateLog("wait_create_log"),//等待生成海报消息
	SuccessCreateLog("success_create_log"),//成功生成海报消息
	FailCreateLog("fail_create_log"),//生成海报失败消息
	FailUploadMediaLog("fail_upload_media_log"),//生成海报失败消息
	SUBSCRIBE_REWARD("subscribe_reward_log"),//关注成功奖励消息
	UNSUBSCRIBE_REWARD("unsubscribe_reward_log"),//取消关注减钱消息
	SUBSCRIBE_REWARD_LEVEL("subscribe_level_reward_log"),//关注成功上级奖励消息
	GET_TASK_LIST_MSG_BEGIN("get_task_list_begin_log"),//关注成功上级奖励消息
	GET_TASK_LIST_MSG_END("get_task_list_end_log"),//关注成功上级奖励消息
	SUBSCRIBE_USER_ACCOUNT("subscribe_user_account"),//用户账号订阅消息
	UNSUBSCRIBE_USER_ACCOUNT("unsubscribe_user_account"),//用户账号取消订阅
	SUBSCRIBE_USER_ACCOUNT_ADD_MONEY("subscribe_user_account_add_money"),//用户关注订阅号成功送钱信息
	SUBSCRIBE_USER_ACCOUNT_SUB_ACCOUNT("subscribe_user_account_sub_account"),//订阅号用户账号订阅消息
	DEFAULT_TEXT_RESPONSE_MSG("default_text_response_msg"),//用户发送文本到订阅号或服务号返回的默认消息。
	SUCCESS_GET_MONEY_TEXT_RESPONSE_MSG("success_get_money_text_response_msg"),//用户发送福利码给订阅号成功赚取到金额时返回的消息
	TASK_IS_NOT_EXIST_TEXT_RESPONSE_MSG("task_is_not_exist_text_response_msg"),//用户发送福利码给订阅号，任务不存在时候返回的消息
	TASK_CODE_ERROR_TEXT_RESPONSE_MSG("task_code_error_text_response_msg"),//用户发送福利码给订阅号，任务不存在时候返回的消息
	TASK_CODE_PATTEN_ERROR_TEXT_RESPONSE_MSG("task_code_patten_error_text_response_msg"),//用户发送福利码给订阅号，福利码格式不对。
	TASK_HAS_FINISHED_TEXT_RESPONSE_MSG("task_has_finished_text_response_msg"),//用户发送福利码给订阅号，任务不存在时候返回的消息
	SUBSCRIBE("subscribe"),//订阅消息
	UNSUBSCRIBE("unsubscribe");//取消订阅




	private String name;
	
	private MsgType(String name) {
	     this.name = name;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
	public static void main(String[] args){
		System.out.println(MsgType.Text);
	}
}
