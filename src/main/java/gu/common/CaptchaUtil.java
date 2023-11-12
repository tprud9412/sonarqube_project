package gu.common;

import nl.captcha.audio.AudioCaptcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.NumbersAnswerProducer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaptchaUtil {
    public CaptchaUtil() {
    }

    public void capthcaImg(HttpServletRequest request, HttpServletResponse response) {
        CaptcCha captcha = new Capthca.Builder(200,60)  // 이미지 크기  200 x 60
                .addText(new NumbersAnswerProducer(6))   // 6자리 숫자
                .addNoise().addNoise().addNoise() // 방해선
                .addBackground(new GradiatedBackgroundProducer()) // 배경색
                .addBorder()  // 테두리
                .build();

        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Max-Age", 0);

    }
    public void captchaAudio(HttpServletRequest request, HttpServletResponse response) {
        String getAnswer = (String) request.getSession().getAttribute("captcha");

        AudioCaptcha ac = new AudioCaptcha.Builder()
                .addAnswer(new SetTextProducer(getAnswer)) // 문자열 전달
                .addVoice()
                .addNoise()   //노이즈
                .build();

        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Max-Age", 0);

        CaptchaServletUtil.writeAudio(response, ac.getChallenge());   // 오디오를 write 한다.

        request.getSession().setAttribute("captcha", ac.getAnswer()); // 값 저장
    }

}