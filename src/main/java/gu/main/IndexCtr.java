package gu.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.common.DateVO;
import gu.common.Field3VO;
import gu.common.Util4calen;
import gu.etc.EtcSvc;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
public class IndexCtr {
    @Autowired
    private IndexSvc indexSvc;
    @Autowired
    private EtcSvc etcSvc;
    
    /**
     * main page. 
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap modelMap) {
        String userno = request.getSession().getAttribute("userno").toString();
        etcSvc.setCommonAttribute(userno, modelMap);

        Date today = Util4calen.getToday(); 

        calCalen(userno, today, modelMap);
        
        
        List<?> listview = indexSvc.selectRecentNews();
        List<?> noticeList = indexSvc.selectNoticeListTop5();
        List<?> listtime = indexSvc.selectTimeLine();
        
        modelMap.addAttribute("listview", listview);
        modelMap.addAttribute("noticeList", noticeList);
        modelMap.addAttribute("listtime", listtime);

        return "main/index";
    }
    
    /**
     * week calendar in main page..
     * Ajax.
     */
    @RequestMapping(value = "/moveDate", method = RequestMethod.GET)
    public String moveDate(HttpServletRequest request, ModelMap modelMap) {
        String userno = request.getSession().getAttribute("userno").toString();
        String date = request.getParameter("date");

        Date today = Util4calen.getToday(date);
        
        calCalen(userno, today, modelMap);
        
        return "main/indexCalen";
    }
    
    private String calCalen(String userno, Date targetDay, ModelMap modelMap) {
        List<DateVO> calenList = new ArrayList<DateVO>();
        
        Date today = Util4calen.getToday();
        int month = Util4calen.getMonth(targetDay);
        int week = Util4calen.getWeekOfMonth(targetDay);
        
        Date fweek = Util4calen.getFirstOfWeek(targetDay);
        Date lweek = Util4calen.getLastOfWeek(targetDay);
        Date preWeek = Util4calen.dateAdd(fweek, -1);
        Date nextWeek = Util4calen.dateAdd(lweek, 1);

    	Field3VO fld = new Field3VO();
    	fld.setField1(userno);
        
        while (fweek.compareTo(lweek) <= 0) {
            DateVO dvo = Util4calen.date2VO(fweek);
            dvo.setIstoday(Util4calen.dateDiff(fweek, today) == 0);
            dvo.setDate(Util4calen.date2Str(fweek));
            
    		fld.setField2(dvo.getDate());
    		dvo.setList( indexSvc.selectSchList4Calen(fld) );
            
            calenList.add(dvo);
            
            fweek = Util4calen.dateAdd(fweek, 1);
        }
        
        modelMap.addAttribute("month", month);
        modelMap.addAttribute("week", week);
        modelMap.addAttribute("calenList", calenList);
        modelMap.addAttribute("preWeek", Util4calen.date2Str(preWeek));
        modelMap.addAttribute("nextWeek", Util4calen.date2Str(nextWeek));

        return "main/index";
    }
    
}
