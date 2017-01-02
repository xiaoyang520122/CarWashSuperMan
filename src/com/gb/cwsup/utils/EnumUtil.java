package com.gb.cwsup.utils;

public class EnumUtil {
	public static String getEnumResult(int paramInt) {
		switch (paramInt) {
		default:
			return "异常数据";
		case 200000:
			return "服务器操作异常";
		case 100001:
			// Constants.loginInfo = null;
			// Constants.getMyHepan = null;
			return "用户登陆超时";
		case 1000002:
			return "用户名或密码错误";
		case 1000003:
			return "用户名不存在";
		case 1000004:
			return "支付密码错误";
		case 1000005:
			return "请先登录";
		case 1000006:
			return "原密码错误";
		case 1000007:
			return "新密码不一致";
		case 1000008:
			return "操作成功";
		case 1000009:
			return "账号绑定失败";
		case 1000:
			return "不能投自己的标";
		case 1001:
			return "请先进行账户充值";
		case 1002:
			return "账户余额不足";
		case 1012:
			return "该标的状态已变";
		case 1013:
			return "投标失败";
		case 1014:
			return "投标成功";
		case 100010:
			return "短信发送成功";
		case 100011:
			return "手机号码已存在";
		case 100012:
			return "手机验证码错误";
		case 100013:
			return "手机允许注册";
		case 100014:
			return "注册成功";
		case 100015:
			return "实名已认证";
		case 100016:
			return "实名未认证";
		case 111125:
			return "认证成功";
		case 111126:
			return "认证失败请确认身份证号码的正确性";
		case 111127:
			return "认证失败该身份证号码已存在";
		case 111113:
			return "登录成功";
		case 111124:
			return "用户名或密码错误";
		case 111128:
			return "身份证后六位与帐号绑定的不匹配";
		case 111129:
			return "实名认证错误";
		case 111130:
			return "银行卡绑定成功";
		case 111131:
			return "银行卡已绑定";
		case 111132:
			return "银行卡未绑定";
		case 111133:
			return "提现申请成功";
		case 111134:
			return "银行编号不能为空";
		case 111135:
			return "市编号不能为空";
		case 111136:
			return "投标时间未到";
		case 111137:
			return "请先绑定手机号";
		case 111138:
			return "短信发送失败";
		case 111139:
			return "身份证号不正确";
		case 111140:
			return "交易密码修改成功";
		case 111141:
			return "交易密码修改失败";
		case 111142:
			return "手机号码不存在";
		case 111143:
			return "密码重置成功";
		}

	}
}
