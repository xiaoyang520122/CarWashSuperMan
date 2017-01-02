package com.gb.cwsup.utils;

import java.util.Random;

import com.gb.cwsup.entity.EngineerOld;
import com.gb.cwsup.entity.GetMyHepan;
import com.gb.cwsup.entity.GetOrderDetail;
import com.gb.cwsup.entity.LoanDesc;
import com.gb.cwsup.entity.LoginInfo;

public class Constants {
	public static final int AUTH_SUCCESS = 20;
	public static final String CLICK_TWICE = "再按一次退出程序";
	public static final int CODE_LOGIN_TRUE = 1;
	public static final int CODE_REQUEST_LOGIN = 1;
	public static final String COLOR_BID_ITEM_AMOUNT = "#A2758C";
	public static final String COLOR_BID_ITEM_RATE = "#FF8400";
	public static final String COLOR_BID_ITEM_TIME = "#7F96A4";
	public static final String COLOR_BLACK = "#000000";
	public static final String COLOR_DASHEDLINE = "#DBD6DA";
	public static final String COLOR_MENU_TEXT = "#898989";
	public static final String COLOR_MENU_TEXT_CLICK = "#76457E";
	public static final String COLOR_ORANGE = "#F29947";
	public static final String COLOR_PROGRESS = "#63cccc";
	public static final String COLOR_PROGRESS_BG = "#63cccc";
	public static final String CONNECT_MSG = "为了确保数据的实时更新，请先联网后再进行后续操作...";
	public static final String CONNECT_TITLE = "联网";
	public static final String DAY = "日";
	public static final String DEFAULT_PASSWORD = "888888";
	public static final String ERROR_DATA = "数据加载异常，请重试...";
	public static final String ERROR_LOGIN_NAME = "用户名不合法";
	public static final String ERROR_LOGIN_PASSWORD = "密码不合法";
	public static final String FULL_BID = "该标已满标";
	public static final String LAST_PAGE = "没有更多数据了...";
	public static final int LIST_COUNT_PER_PAGE = 20;
	public static final String LOADING = "请稍后...";
	public static final String LOGIN = "登录";
	public static final String LOGINING = "正在登录，请稍后...";
	public static final String LOGIN_FIRST = "请先登录...";
	public static final long MAX_GESTURE_TIME = 10000L;
	public static final String MONTH = "月";
	public static final String NULL_DATA = "暂无数据";
	public static final String NULL_LOGIN_NAME = "用户名不能为空";
	public static final String NULL_LOGIN_PWD = "密码不能为空";
	public static final String OK = "已联网";
	public static final String PERCENT = "%";
	public static final String REGEX_EMAIL = "^(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)|(\\?)$";
	public static final String REGEX_ENGLISH_CHINESE_NUM = "^[a-zA-Z0-9._一-龥]{1,10}$";
	public static final String REGEX_ENUM = "^[0-9]{2,10}$";
	public static final String REGEX_LOGIN_NAME = "^[a-zA-Z0-9_@.]{4,24}$";
	public static final String REGEX_LOGIN_PASSWORD = "^[a-zA-Z0-9_.]{3,16}$";
	public static final String REGEX_MONEY = "^([1-9]{1}\\d*)(\\.{1}(\\d){1,2})?$";
	public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
	public static final String REGEX_PHONE = "^((\\(\\d{3}\\))|(\\d{3}\\-))?1(3|4|5|8)\\d{9}|(\\?){1,1}|(10000000000)$";
	public static final String REQUEST_FAIL = "请求失败，请重试...";
	public static final String RMB_CHINESE = "元";
	public static final String SIM_STATE_ABSENT = "没有SIM卡或SIM卡不可用，请插入SIM卡后再进行操作";
	public static final String SIM_STATE_UNKNOWN = "SIM卡状态未知，请检查您的SIM卡后再进行操作";
	public static final String TYPE_DAY = "天";
	public static final String TYPE_MONTH = "月";
	public static final String WAN = "万";
	public static final String YEAR = "年";
	public static final String[] arrayBidType = { "信用标", "抵押标", "转让标", "慈善标",
			"专项标", "实地认证标", "校园大赛标" };
	public static final String[] arrayRefundType;
	// public static List<AuthInfo> authRecordList;
	public static String[] bankArray;
	public static String[] bankBranchArray;
	// public static BankCardListM bankCardInfo;
	// public static List<BidRecord> bidRecordList;
	// public static BorrowerInfo biderInfo;
	public static String[] cityArray;
	public static long currGestureTime;
	// public static DailyOrder dailyOrder;
	public static GetMyHepan getMyHepan;
	 public static GetOrderDetail getOrderDetail;
	 public static EngineerOld engineer;
	// public static List<GetOrderList> getOrderList;
	public static boolean hasNewVersion = false;
	public static int investTabYouPage;
	public static boolean isNecessaryToUpdate;
	public static long lastGestureTime;
	public static LoanDesc loanDesc;
	public static LoginInfo loginInfo;
	// public static NoticeItem noticeItem;
	public static String[] provinceArray;

	public static double getRandomNum(double from, double to) {
		Random random = new Random();

		double s = random.nextDouble();
		return s * (to - from) + from;
	}
//
//	public static ArrayList<EngineerEntity> getEngineerList(double d, double e) {
//		ArrayList<EngineerEntity> newsList = new ArrayList<EngineerEntity>();
//		String[] names = new String[] { "车余达", "邓昔革", "区愿灿", "张文管", "凌东木",
//				"欧勤皋", "骆鲁书", "蔡阐晨", "凌佛赞", "于笛潇", "骆召原", "赵管俭", "王敞桦", "葛咏轩",
//				"吕超炳", "于必湘", "黎佳羽", "梁倌争", "李慈家", "王修睿", "陈效冬", "何暮策", "齐裕浪",
//				"颜雄治", "张彬勳", "韩策冠", "区灶鲁", "丁兢俭", "王厉庆", "郎嘉庆", "赖申万", "殷城",
//				"云霖风", "彭暗耀", "金旺安", "冯琅火", "翁庄洲", "关樵枫", "樊鹏桥", "梁宜光", "蔡岩咏",
//				"吴标贝", "符建修", "赵纤棉", "郎福蜀", "周炼泉", "蔡仁赣", "田皓", "张更宪", "龚卉羿" };
//		for (int i = 1; i <= 10; i++) {
//			EngineerEntity engineer = new EngineerEntity();
//			// int num = (int) Constants.getRandomNum(0, 49);
//
//			engineer.setName(names[i]);
//			engineer.setLevel(String.valueOf(Constants.getRandomNum(3, 5)));
//			engineer.setLatitude((d + Constants.getRandomNum(-0.005, 0.005)));
//			engineer.setLongtitude((e + Constants.getRandomNum(-0.005, 0.005)));
//			engineer.setEngineer_id("SZ100"
//					+ (int) Constants.getRandomNum(10, 80));
//			engineer.setPicture_small("http://www.online4s.com/ditu/engineer/introduce2_"
//					+ i + ".png");
//			engineer.setYear(String.valueOf((int) Constants.getRandomNum(0, 20)));
//			engineer.setDomicile("深圳");
//			engineer.setFees_home(50);
//			engineer.setFees_time(150);
//			engineer.setDistance((int) Constants.getRandomNum(0, 20) + "KM");
//			engineer.setService_times((int) Constants.getRandomNum(5, 100));
//			engineer.setChexi("德系车");
//			// news.setId(i);
//			// news.setNewsId(i);
//			// news.setCollectStatus(false);
//			// news.setCommentNum(i + 10);
//			// news.setInterestedStatus(true);
//			// news.setLikeStatus(true);
//			// news.setReadStatus(false);
//			// news.setNewsCategory("推荐");
//			// news.setNewsCategoryId(1);
//			// news.setTitle("可以用谷歌眼镜做的10件酷事：导航、玩游戏");
//			List<String> url_list = new ArrayList<String>();
//
//			newsList.add(engineer);
//		}
//		return newsList;
//	}

	static {
		// dailyOrder = null;
		investTabYouPage = 1;
		// getOrderList = null;
		lastGestureTime = 0L;
		currGestureTime = System.currentTimeMillis();
		isNecessaryToUpdate = false;
		arrayRefundType = new String[] { "等额本息", "按月付息,到期还本", "等额本金",
				"到期一次还本付息" };
	}
}
