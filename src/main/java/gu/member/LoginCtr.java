package gu.member;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gu.common.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.member.UserVO;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class LoginCtr {    
    private static final Integer cookieExpire = 60 * 60 * 24 * 30; // 1 month
    
    @Autowired
    private MemberSvc memberSvc;
    
    /**
     * 로그인화면.
     */
    @RequestMapping(value = "memberLogin", method = RequestMethod.GET)
    public String memberLogin(HttpServletRequest request, ModelMap modelMap) {
        String userid = get_cookie("sid", request);

        modelMap.addAttribute("userid", userid);

        return "member/memberLogin";
    }
    
    /**
     * 로그인 처리.
     */
    @RequestMapping(value = "memberLoginChk", method = RequestMethod.POST)
    public String memberLoginChk(HttpServletRequest request,HttpServletResponse response, LoginVO loginInfo, ModelMap modelMap) {

        UserVO mdo = memberSvc.selectMember4ID(loginInfo);

        //login
        if (mdo == null) {
            modelMap.addAttribute("msg", "로그인할 수 없습니다. 아이디를 확인해주세요.");
            return "common/message";
        }

        if (mdo.getFailcnt() > 4) {
            modelMap.addAttribute("msg", "사용중지된 계정입니다, 관리자에게 문의하세요.");
            return "common/message";
        }

        if (memberSvc.selectMember4Login(loginInfo) == null) {
            memberSvc.updateLoginFailCnt(mdo, mdo.getFailcnt() + 1);
            modelMap.addAttribute("msg", "로그인할 수 없습니다. 비밀번호를 확인해주세요.");
            return "common/message";
        }

        memberSvc.updateLoginFailCnt(mdo, 0);
        memberSvc.insertLogIn(mdo.getUserno());
        
        HttpSession session = request.getSession();

        session.setAttribute("userid",mdo.getUserid());
        session.setAttribute("userrole",mdo.getUserrole());
        session.setAttribute("userno",mdo.getUserno());
        session.setAttribute("usernm",mdo.getUsernm());
        
        if ("Y".equals(loginInfo.getRemember())) {
            set_cookie("sid", loginInfo.getUserid(), response);
        } else { 
            set_cookie("sid", "", response);       
        }

        return "redirect:/index";
    }

    @RequestMapping(value = "captchaImg.do")
    public void captchaImg(HttpServletRequest request, HttpServletResponse response) throws Exception{
        new CaptchaUtil().capthcaImg(request,response);
    }
    /**
     * 소리 자동방지
     */
    @RequestMapping(value = "captchaAudio.do")
    public void captchaAudio(HttpServletRequest request, HttpServletResponse response) throws Exception{
        new CaptchaUtil().captchaAudio(request,response);
    }

    
    /**
     * 로그아웃.
     */
    @RequestMapping(value = "memberLogout", method = RequestMethod.GET)
    public String memberLogout(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        
        String userno = session.getAttribute("userno").toString();
        
        memberSvc.insertLogOut(userno);
        
        session.removeAttribute("userid"); 
        session.removeAttribute("userrole");        
        session.removeAttribute("userno");        
        session.removeAttribute("usernm");
        session.removeAttribute("mail");
        
        return "redirect:/memberLogin";
    }
    
    /** 
     * 사용자가 관리자페이지에 접근하면 오류 출력.
     */
    @RequestMapping(value = "noAuthMessage", method = RequestMethod.GET)
    public String noAuthMessage(HttpServletRequest request) {
        return "common/noAuth";
    }
  
    /*
     * -------------------------------------------------------------------------
     */
    /**
     * 쿠키 저장.     
     */
    public static void set_cookie(String cid, String value, HttpServletResponse res) {

        Cookie ck = new Cookie(cid, value);
        ck.setPath("/");
        ck.setMaxAge(cookieExpire);

        // HttpOnly 플래그 설정
        ck. setHttpOnly(true);

        // Secure 플래그 설정
        ck.setSecure(true);

        res.addCookie(ck);        
    }

    /**
     * 쿠키 가져오기.     
     */
    public static String get_cookie(String cid, HttpServletRequest request) {
        String ret = "";

        if (request == null) {
            return ret;
        }
        
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ret;
        }
        
        for (Cookie ck : cookies) {
            if (ck.getName().equals(cid)) {
                ret = ck.getValue();
                
                ck.setMaxAge(cookieExpire);
                break; 
            }
          }
        return ret; 
    }

}
