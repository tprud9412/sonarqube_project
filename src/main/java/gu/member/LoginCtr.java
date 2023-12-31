package gu.member;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gu.common.CaptchaUtil;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.member.UserVO;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String memberLoginChk(HttpServletRequest request, HttpServletResponse response, LoginVO loginInfo, ModelMap modelMap) {

        UserVO mdo = memberSvc.selectMember4ID(loginInfo);

        //ID Check
        if (mdo == null) {
            modelMap.addAttribute("msg", "로그인할 수 없습니다. 아이디 및 비밀번호를 확인해주세요.");
            return "common/message";
        }

        //PW Check
        if (memberSvc.selectMember4Login(loginInfo) == null) {
            modelMap.addAttribute("msg", "로그인할 수 없습니다. 아이디 및 비밀번호를 확인해주세요.");
            return "common/message";
        }

        //Captcha Check
        Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        String ans = request.getParameter("answer");

        if(ans!=null && !"".equals(ans)) {
            if(captcha.isCorrect(ans)) {
                request.getSession().removeAttribute(Captcha.NAME);
            }else {
                modelMap.addAttribute("msg", "보안방지문자가 일치하지 않습니다.");
                return "common/message";
            }
        }


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

    @RequestMapping(value = "captchaImg", method = RequestMethod.GET)
    public void captchaImg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("test" + request);
        new CaptchaUtil().getImgCaptCha(request,response);
    }
    /**
     * 전달받은 문자열로 음성 가져오는 메서드
     */
    @RequestMapping(value = "captchaAudio", method = RequestMethod.GET)
    public void captchaAudio(HttpServletRequest request, HttpServletResponse response){
        Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        String getAnswer = captcha.getAnswer();
        new CaptchaUtil().getAudioCaptCha(request, response, getAnswer);
    }

    /**
     * 사용자가 입력한 보안문자 체크하는 메서드
     */
    @RequestMapping(value = "chkAnswer", method = RequestMethod.POST)
    @ResponseBody
    public String chkAnswer(HttpServletRequest request, HttpServletResponse response) {
        String result = "";

        Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        String ans = request.getParameter("answer");

        if(ans!=null && !"".equals(ans)) {
            if(captcha.isCorrect(ans)) {
                result = "200";
            }else {
                result = "300";
            }
        }
        return result;
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
