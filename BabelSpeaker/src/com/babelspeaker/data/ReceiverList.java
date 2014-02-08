package com.babelspeaker.data;

public class ReceiverList {
    public final static String LOAD_RECEIVER = "com.babelspeaker.LoadReceiver";
    public final static String BASE_RECEIVER = "com.babelspeaker.BaseReceiver";

    public class Account {
        public final static String MAIN_RECEIVER = "com.babelspeaker.AccountMainReceiver";
        public final static String UPDATE_SERVICE_RECEIVER = "com.babelspeaker.AccountUpdateServiceReceiver";
        public final static String REGISTER_RECEIVER = "com.babelspeaker.AccountRegisterReceiver";
        public final static String CHANGE_PASSWORD_RECEIVER = "com.babelspeaker.AccountChangePasswordReceiver";
        public final static String RECHARGE_RECEIVER = "com.babelspeaker.AccountRechargeReceiver";
        public final static String FIND_PASSWORD_RECEIVER = "com.babelspeaker.AccountFindPasswordReceiver";
        public final static String RECHARGE_HISTORY_RECEIVER = "com.babelspeaker.AccountRechargeHistoryReceiver";
        public final static String CONSUME_HISTORY_RECEIVER = "com.babelspeaker.AccountConsumeHistoryReceiver";
    }

    public class Quotes {
        public final static String MAIN_RECEIVER = "com.babelspeaker.QuotesMainReceiver";
        public final static String CHART_RECEIVER = "com.babelspeaker.QuotesChartReceiver";
    }
    
    public class News {
        public final static String FINANCE_CALENDAR_RECEIVER = "com.babelspeaker.NewsFinanceCalendarReceiver";
        public final static String ANNOUNCEMENT_RECEIVER = "com.babelspeaker.NewsAnnouncementReceiver";
        public final static String INSTITUTION_REVIEW_RECEIVER = "com.babelspeaker.InstitutionalReviewReceiver";
        public final static String EXCHANGE_INVESTMENT_RECEIVER = "com.babelspeaker.ExchangeAndInvestmentReceiver";
        public final static String PUPIL_QUOTES_DATA_RECEIVER = "com.babelspeaker.PupilQuotesDataReceiver";
        public final static String PUPIL_QUOTES_DETAIL_DATA_RECEIVER = "com.babelspeaker.PupilQuotesDetailDataReceiver";
        public final static String EXCLUSIVE_POLICY_RECEIVER = "com.babelspeaker.ExclusivePolicyReceiver";
        public final static String PREMIUM_RECEIVER = "com.metlas.PremiumReceiver";
    }
    
    public class More {
        public final static String METALS_HISTORY_RECEIVER = "com.babelspeaker.MetalsHistoryReceiver";
        public final static String PRICE_CONVERSION_RECEIVER = "com.babelspeaker.PriceConversionReceiver";
        public final static String FEEDBACK_RECEIVER = "com.babelspeaker.feedbackReceiver";
        public final static String CONTACT_US_RECEIVER = "com.babelspeaker.ContactUsReceiver";
        public final static String BRAND_GOLD_RECEIVER = "com.babelspeaker.BrandGoldRecevier";
        public final static String ETF_RECEIVER = "com.babelspeaker.EtfReceiver";
        public final static String TD_CHARGES_RECEIVER = "com.babelspeaker.TdChargesReceiver";
        public final static String TD_GAIN_LOSS_RECEIVER = "com.babelspeaker.TDGainLossReceiver";
    }
    
    public class Services {
        public final static String INTRADY_GUIDE_RECEIVER = "com.babelspeaker.IntradyGuideReceiver";
        public final static String INTRADY_GUIDE_DETAIL_RECEIVER = "com.babelspeaker.IntradyGuideDetailReceiver";
        public final static String REAL_TRADE_RECEIVER = "com.babelspeaker.RealTradeReceiver";
        public final static String QUOTES_REMIND_RECEIVER = "com.babelspeaker.QuotesRemindReceiver";
        public final static String QUOTES_REMIND_DETAIL_RECEIVER = "com.babelspeaker.QuotesRemindDetailReceiver";
        public final static String LOCAL_RING_RECEIVER = "com.babelspeaker.LocalRingReceiver";
        public final static String TRADE_RULE_RECEIVER = "com.babelspeaker.tradeRuleRecevier";
        public final static String ACCOUNT_APPLY_RECEIVER = "com.babelspeaker.AccountApplyReceiver";
    }
    
    public class Mall {
        public final static String MALL_MAIN_RECEIVER = "com.babelspeaker.mallMainReceiver";
        public final static String MALL_GET_RECEIVER = "com.babelspeaker.mallGetReceiver";
        public final static String MALL_PRODUCT_RECEIVER = "com.babelspeaker.mallProductReceiver";
        public final static String MALL_SUBMIT_INFO_RECEIVER = "com.babelspeaker.mallSubmitInfoReceiver";
    }
    
    public class Widget {
        public final static String WIDGET_SINGLE_PRICE_RECEIVER = "com.babelspeaker.WidgetSinglePriceReceiver";
    }
}