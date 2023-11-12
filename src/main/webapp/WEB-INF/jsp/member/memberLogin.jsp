<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><s:message code="common.pageTitle"/></title>

    <link href="css/sb-admin/bootstrap.min.css" rel="stylesheet">
    <link href="css/sb-admin/metisMenu.min.css" rel="stylesheet">
    <link href="css/sb-admin/sb-admin-2.css" rel="stylesheet">
    <link href="css/sb-admin/font-awesome.min.css" rel="stylesheet">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script src="js/jquery-2.2.3.min.js"></script>
    <script src="css/sb-admin/bootstrap.min.js"></script>
    <script src="css/sb-admin/metisMenu.min.js"></script>
    <script src="css/sb-admin/sb-admin-2.js"></script>
	<script src="js/project9.js"></script>    
<script>
function fn_formSubmit(){
	if ( ! chkInputValue("#userid", "<s:message code="common.id"/>")) return false;
	if ( ! chkInputValue("#userpw", "<s:message code="common.password"/>")) return false;
	
	$("#form1").submit();
}
</script>

<script>
    // 새로고침시 이미지 변경되고, 음성듣기 클릭시 음성이 들리는 기능 구현
    function audio(){
        var rand = Math.random();
        var url = 'captchaAudio.do'

        $.ajax({
            url : url,
            type : 'POST',
            dataType : 'text',
            data : 'rand=' + rand,
            async : false,
            success : function(resp){
                var uAgent = navigator.userAgent;
                var soundUrl = 'captchaAudio.do?rand=' + rand;
                // 브라우저별 오디오 처리
                if (uAgent.indexOf('Trident') > -1 || uAgent.indexOf('MSIE') > -1) {    //IE인 경우
                    winPlayer(soundUrl);
                } else if (!!document.createElement('audio').canPlayType){
                    try {
                        new Audio(soundUrl).play();
                    } catch (e) {
                        winPlayer(soundUrl);
                    }
                } else {
                    window.open(soundUrl, '', 'width=1,height=1');
                }
            }
        });
    }

    function refreshBtn(type){
        var rand = Math.random();
        var url = 'captchaImg.do?rand=' + rand;

        $('#captchaImg').attr("src", url);
    }

    function winPlayer(objUrl){
        $("#captchaAudio").html(' <bgsoun src="' + objUrl + '">');       //bgsound 배경음악 제어
    }
</script>

</head>

<body>

    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                    </div>
                    <div class="panel-body">
                        <form role="form" action="memberLoginChk" method="post" id="form1" name="form1">
                            <fieldset>
                                <legend>로그인</legend>
                                <div class="form-group">
                                    <input class="form-control" placeholder="ID" name="userid" id="userid" type="email" autofocus value="<c:out value="${userid}"/>">
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Password" name="userpw" id="userpw" type="password" value="" onkeydown="if(event.keyCode == 13) { fn_formSubmit();}">
                                </div>

                                <div class="form-group">
                                    <label style="display:block">자동 로그인 방지</label>
                                    <div class="captcha">
                                        <div class="captcha_child">
                                            <img id ="captchaImg" title ="캡차 이미지" src="captchaImg.do" alt="캡차 이미지"/>
                                            <div id ="captchaAudio" style="display:none"></div>
                                        </div>
                                        <div class="captcha_child_two">
                                            <a onclick="javaScript:refreshBtn()" class="refreshBtn">
                                                <i class="fa fa-refresh" aria-hidden="true"></i> 새로고침
                                            </a>
                                            <a onclick="javaScript:audio()" class="refreshBtn">
                                                <i class="fa fa-volum-up" aria-hidden="true"></i> 음성듣기
                                            </a>
                                        </div>
                                    </div>
                                </div>

                                <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="Y"  <c:if test='${userid != null && userid != ""}'>checked</c:if>>Remember Me
                                    </label>
                                </div>
                                <!-- Change this to a button or input when using this as a form -->
                                <a href="#" class="btn btn-lg btn-success btn-block" onclick="fn_formSubmit()">Login</a>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

</html>
