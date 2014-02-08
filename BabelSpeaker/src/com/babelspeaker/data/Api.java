package com.babelspeaker.data;

/**
 * 接口定义
 * @author mark
 *
 */
public class Api {
//    public static String MALL_SITE = "http://112.124.26.68/zsgjs2/trunk/shop/service/A";
//    public static String SITE = "http://112.124.26.68/zsgjs2/trunk/Mobi/A";
    public static String SITE = "http://hg01.sinaapp.com/Mobi/A";
    public static String MALL_SITE = "http://hg01.sinaapp.com/shop/service/A";
//    public static String SITE = "http://2.zsgjs.sinaapp.com/Mobi/A";
//    public static String SITE = "http://8.zhj8.sinaapp.com//Mobi/A";
    public static class Init {
        public static String BIND_APP = SITE + "/InitApp/OpenApp?mobitype=@phone&mobisys=android&osnum=@osnum&deviceId=@deviceid&appnum=@appnum&os=android&uid=@uid&st=@st";
        public static String SET_TOKENID = SITE + "/InitApp/SetDeviceTokendId?deviceId=@deviceId&tokenId=@tokenId";
    }
    
    public static class Account {
        public static String REGISTER = InitData.SITE + "/Index/Register/p/";
        public static String LOGIN = SITE + "/User/Login?key=@key&pwd=@pwd&deviceId=@dev&os=android";
        public static String GET_MESSAGE = SITE + "/User/GetUserMessage?id=@id&safetycode=@safety&os=android";
        public static String SEND_VERIFICATION =  SITE + "/User/GetMobiCode?os=android&mobi=";
        public static String REGISTER_BY_PHONE = SITE + "/User/MobiRegister?mobi=@p&code=@c&pwd=@wd&os=android&nickName=@nickName";
        public static String REGISTER_BY_QQ = SITE + "/User/QQRegister?qq=@p&pwd=@wd&os=android&nickName=@nickName";
        public static String MODIFY_PASSWORD = SITE + "/User/ModifyPwd?id=@id&oldpwd=@old&newspwd=@new&safetycode=@safety&os=android";
        public static String MODIFY_USER_MESSAGE = SITE + "/User/ModifyUserMessage?id=@id&nickName=@nickName&realName=@realName&safetycode=@safety&os=android";
        public static String MODIFY_MOBI = SITE + "/User/ModifyMobi?id=@id&mobi=@mobi&code=@code&os=android&safetycode=@safety";
        public static String MODIFY_QQ = SITE + "/User/ModifyQQ?id=@id&qq=@qq&safetycode=@safety&os=android";
        public static String FIND_PASSWORD = SITE + "/User/FindPwd?key=@key&type=@type&os=android";
        public static String MODIFY_PASSWORD_BY_PHONE = SITE + "/User/ModifyMobiPwd?mobi=@mobi&code=@code&pwd=@password&os=android";
        public static String ACCOUNT_LEVEL_LIST = SITE + "/User/UserLevelist?os=android";
        
        public static String RECHARGE_LIST = SITE + "/Upay/product?os=android";
        public static String RECHARGE_ORDER = SITE + "/Upay/pay?os=android&amount=@amount&uid=@uid&safetycode=@safety";
        public static String RECHARGE_UPDATE = SITE + "/Upay/paySuccess?os=android&trade_no=@trade_no&uid=@uid&safeCode=@safety";
        public static String RECHARGE_HISTORY =SITE + "/Upay/payRecord?os=android&uid=@uid&safetycode=@safety";
        public static String CONSUME_HISTORY = SITE + "/Upay/expenseRecord?os=android&uid=@uid&safetycode=@safety";
    }
    
    
    public static class Quotes {
        public static String GROUP = InitData.SITE + "/Api/group?os=android";
        public static String TDATA = InitData.SITE + "/Api/tdata?os=android&tid=";
        public static String TDATAS = InitData.SITE + "/Api/tdatas?os=android&tids=";
        public static String GDATA = InitData.SITE + "/Api/gdata?os=android&gid=";
//        public static String KDATA = InitData.SITE + "/Api/kdata/tid/XHAG/tt/1015";
        public static String KDATA = InitData.SITE + "/Api/kdata?os=android&tid=@tid&tt=@tt";
        public static String TSDATA = InitData.SITE + "/Api/trendData?os=android&tid=@tid&t=@t";
        public static String INDEX_DATA = SITE + "/InitApp/IndexPageData?os=android&tid=";
        public static String PREMIUM_DATA = InitData.SITE + "/Api/premium?os=android";
        public static String QUOTES_IS_DEALING = InitData.SITE + "/Api/isDeal?tid=@tid&os=android";
        public static String BRAND_GOLD = InitData.SITE + "/Api/gold?os=android";
        public static String ETF_GOLD_DATA = InitData.SITE + "/Api/etfAu?os=android";
        public static String ETF_SILVER_DATA = InitData.SITE + "/Api/etfAg?os=android";
        public static String TD_DEFERRED_CHARGES = InitData.SITE + "/Api/jsj?os=android";
    }
    
    public static class News {
        public static String FINANCE_DATA = SITE + "/Finance/index?id=@id&os=android";
        public static String ANNOUNCEMENT_COUNT = SITE + "/Notice/CheckNoticeDate?id=@id&os=android";
        public static String ANNOUNCEMENT_DATA = SITE + "/Notice/NoticeList?os=android";
        public static String INSTITUTION_REVIEW_DATA = SITE + "/Comment/CommentList?os=android&lastid=";
        public static String INSTITUTION_REVIEW_DETAIL_DATA = SITE + "/Comment/GetcommentContent?os=android&id=";
        public static String PUPIL_QUOTES_DATA = SITE + "/Market/MarketList?os=android";
        public static String PUPIL_QUOTES_DETAIL_DATA = SITE + "/Market/MarketContent?os=android&id=@id";
        public static String EXCHANGE_DATA = SITE + "/Bourse/BourseList?os=android";
        public static String EXCHANGE_DETAIL_DATA = SITE + "/Bourse/GetBourseContent?os=android&id=@id";
        public static String INVESTMENT_DATA = SITE + "/Inverstor/InverstorList?os=android";
        public static String INVESTMENT_DETAIL_DATA = SITE + "/Inverstor/GetInverstorContent?os=android&id=@id";
        public static String DAILY_EXPRESS_DATA = SITE + "/DailyNew/DailyNewList?os=android&lastid=";
        public static String DAILY_EXPRESS_DETAIL_DATA = SITE + "/DailyNew/news?os=android&id=@id";
        public static String DAILY_EXPRESS_CHECK = SITE + "/DailyNew/checkNews?newid=";
        public static String DEPTH_ANALYSIS_DATA = SITE + "/HightResolut/ResolutList?os=android&lastid=";
        public static String DEPTH_ANALYSIS_DETAIL = SITE + "/HightResolut/ResolutContent?id=@id&os=android";
        public static String TACTICS_DATA = SITE + "/Strate/ZJStrateList?os=android&lastid=";
    }
    
    public static class More {
        public static String GOLD_HISTORY = SITE + "/StringImg/Au?os=android";
        public static String SILVER_HISTORY = SITE + "/StringImg/Ag?os=android";
        public static String AU_AG_USD_OIL = SITE + "/StringImg/fossil?os=android";
        public static String FEEDBACK_LIST = SITE + "/FeedBack/FeedList?os=android&deviceid=@id";
        public static String ADD_FEEDBACK = SITE + "/FeedBack/AddFeed?os=android&deviceid=@id&message=@msg&uid=@uid&safetyCode=@safety";
        public static String CONTACT_PAGE = SITE + "/Contact/getPage?os=android&key=@key";
        public static String CONTACT_PHONE = SITE +"/Contact/getPhone?os=android&key=@key";
    }
    
    public static class Services {
//        private static String site = "http://112.124.26.68/zsgjs2/trunk/Mobi/A";
        public static String INTRADY_GUIDE = SITE + "/Guide/glist?os=android";
//        public static String INTRADY_GUIDE = SITE + "/DiskGuide/DiskGuideList?os=android";
        public static String INTRADY_GUIDE_FEEDBACK = SITE + "/Guide/reply?os=android&gid=@pid&reply=@ct&uid=@uid&uname=@cn&safetycode=@sc";
        public static String INTRADY_GUIDE_DETAIL = SITE + "/Guide/rlist?gid=@id&os=android";
        
        
        public static String REAL_TRADE = SITE + "/FirmBagain/FirmBagainList?os=android";
        public static String SIMULATION_TRADE = SITE + "/Simulate/simulateList?os=android";
        public static String TRADE_RULE = SITE + "/Traderules/traderulesList?os=android";
        public static String TRADE_RULE_DETAIL = SITE + "/Traderules/GettraderulesContent?os=android&id=";
        public static String QUOTES_REMIND = SITE + "/PushSet/allTips?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_DETAIL = SITE + "/PushSet/getSetting?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_SWITCH = SITE + "/PushSet/switchFloat?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_FLOAT = SITE + "/PushSet/setFloat?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_TIME_GET = SITE + "/PushSet/getSetTime?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_TIME_SET = SITE + "/PushSet/setTime?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_POINT_SET = SITE + "/PushSet/setPoint?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_POINT_DELETE = SITE + "/PushSet/delPoint?os=android&key=@key&safetyCode=@safety";
        public static String QUOTES_REMIND_SAVE_INFO = SITE + "/PushSet/saveInfo?os=android&key=@key&safetyCode=@safety";
        
        public static String ACCOUNT_APPLY_LIST = SITE + "/Apply/alist?os=android";
        public static String ACCOUNT_APLY_ADD = SITE + "/Apply/asend?os=android&p=";
    }
    
    public static class WebUrl {
        public static String OFFICAL_ACTIVITY = SITE + "/OfficeAct/officeActiveList?os=android";
        public static String DISCLAIMER = SITE + "/Disclaimer/Disclaimer?os=android";
        public static String INSTRUCTION = SITE + "/Instruct/instruction?os=android";
        public static String ABOUT_US = SITE + "/About/aboutus?os=android";
        public static String VOICE = SITE + "/Speek/speeking?os=android";
        public static String UPDATE_HISTORY = SITE + "/UpdatHistory/updatehistory?os=android";
        public static String ACCOUNT_APPLY = SITE + "/Apply/countApply/alist?os=android";
    }
    
    public static class Mall {
        public static String MALL_AD = MALL_SITE + "/ShopOperate/RequestAd?os=android";
        public static String MALL_PRODUCT_DETAIL = MALL_SITE + "/ShopOperate/ProductDetail?os=android&productid=@pid";
        public static String MALL_PRODUCT_LIST = MALL_SITE + "/ShopOperate/RequestProductList?os=android";
        public static String MALL_PRODUCT_GOT_LIST = MALL_SITE + "/ShopOperate/RequestOrders?os=android&uid=@uid&safetycode=@safety";
        public static String MALL_CONTACT_INFO = MALL_SITE + "/ShopOperate/RequestContactInfo?os=android&uid=@uid&safetycode=@safety";
        public static String MALL_REQUEST_ORDER = MALL_SITE + "/ShopOperate/Order?os=android&uid=@uid&safetycode=@safety&productid=@pid&num=@num&name=@name&address=@address&postcode=@code&tel=@tel&contactid=@contactid";
        public static String MALL_CANCEL_ORDER = MALL_SITE + "/ShopOperate/CancelOrder?os=android&uid=@uid&safetycode=@safety&orderid=@orderId";
    }
}
