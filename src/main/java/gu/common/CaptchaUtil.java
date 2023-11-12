package gu.common;

import nl.captcha.Captcha;
import nl.captcha.audio.AudioCaptcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.NumbersAnswerProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaptchaUtil {

    private static int width = 150;	/*보안문자 이미지 가로크기*/
    private static int height = 50; /*보안문자 이미지 세로크기*/

    /*CaptCha Image 생성*/
    public void getImgCaptCha(HttpServletRequest req, HttpServletResponse res) {
        /*폰트 및 컬러 설정*/
        List<Font> fontList = new ArrayList<Font>();
        fontList.add(new Font("", Font.HANGING_BASELINE, 40));
        fontList.add(new Font("Courier", Font.ITALIC, 40));
        fontList.add(new Font("", Font.PLAIN, 40));
        List<Color> colorList = new ArrayList<Color>();
        colorList.add(Color.BLACK);

        Captcha captcha = new Captcha.Builder(width,  height)
                // .addText() 또는 아래와 같이 정의 : 6자리 숫자와 폰트 및 컬러 설정
                .addText(new NumbersAnswerProducer(6), new DefaultWordRenderer(colorList, fontList))
                .addNoise().addBorder()
                .addBackground(new GradiatedBackgroundProducer())
                .build();

        res.setHeader("Cache-Control", "no-store");
        res.setHeader("Pragma", "no-cache"); // 브라우저 캐쉬를 지우기 위한 헤더값 설정

        res.setDateHeader("Expires", 0);
        res.setContentType("image/jpeg"); // 리턴값을 image 형태로 설정

        /*JSP에서 Captcha 객체에 접근할 수 있도록 session에 저장*/
        req.getSession().setAttribute(Captcha.NAME, captcha);
        // 자동가입 문자 Image를 생성한다
        CaptchaServletUtil.writeImage(res, captcha.getImage());


        // Captcha가 생성한 자동가입방지 문자를 return 받아서 String 변수에 할당
        String captcha_str = captcha.getAnswer();
        //*************************************************************

        //검증화면에서 '사용자 입력값'과 '자동가입방지문자'를 비교할 수 있도록 Session에 저장한다.
        req.getSession().setAttribute("captcha", captcha_str);

        /*test*/System.out.println("captcha 자동가입방지 문자 : " + captcha_str);
    }

    /*CaptCha Audio 생성*/
    public void getAudioCaptCha(HttpServletRequest req, HttpServletResponse res, String answer){
        HttpSession session = req.getSession();

        Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
        String getAnswer = answer;

        if(getAnswer == null || getAnswer.equals("")) getAnswer = captcha.getAnswer();

        AudioCaptcha audiocaptcha = new AudioCaptcha.Builder()
                .addAnswer(new SetTextProducer(getAnswer))
                .build();

        CaptchaServletUtil.writeAudio(res,  audiocaptcha.getChallenge());
    }

}