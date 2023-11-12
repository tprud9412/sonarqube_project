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
        function fn_formSubmit() {
            if (!chkInputValue("#userid", "<s:message code="common.id"/>")) return false;
            if (!chkInputValue("#userpw", "<s:message code="common.password"/>")) return false;

            $("#form1").submit();
        }

        window.onload = function(){
            getImage();	// 이미지 가져오기
            document.querySelector('#check').addEventListener('click', function(){
                var params = {answer : document.querySelector('#answer').getAttribute('value')};

                $.ajax({
                    url: "chkAnswer",
                    type:"post",
                    data : {
                        answer:$("#answer").val(),
                    },
                    success: function(returnData){
                        if(returnData == 200){
                            alert('입력값이 일치합니다.');
                            // 성공 코드
                        }else {
                            alert('입력값이 일치하지 않습니다.');
                            getImage();
                            document.querySelector('#answer').setAttribute('value', '');
                        }}
                }, 'json');

            });
        }

        function audio(){
            var rand = Math.random();
            var uAgent = navigator.userAgent;
            var soundUrl = '${ctx}/captchaAudio?rand='+rand;

            if(uAgent.indexOf('Trident')>-1 || uAgent.indexOf('MISE')>-1){	/*IE 경우 */
                audioPlayer(soundUrl);
            }else if(!!document.createElement('audio').canPlayType){ /*Chrome 경우 */
                try {
                    new Audio(soundUrl).play();
                } catch (e) {
                    audioPlayer(soundUrl);
                }
            }else{
                window.open(soundUrl,'','width=1,height=1');
            }
        }

        function getImage(){
            var timestamp = new Date().getTime(); // 현재 시간을 이용하여 timestamp 생성
            var url = '${ctx}/captchaImg?timestamp=' + timestamp;

            $.ajax({
                url: url,
                type: 'GET',
                success: function(data) {
                    // 이미지 업데이트 또는 다른 필요한 동작 수행
                    $("#captchaImage").attr("src", url);
                },
                error: function(error) {
                    console.error('Error fetching captcha image:', error);
                }
            });
        }

        function audioPlayer(objUrl){
            document.querySelector('#ccaudio').innerHTML = '<bgsoun src="' +objUrl +'">';
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

                                <label style="display:block">자동 로그인 방지</label>
                                <div style="overflow:hidden">
                                    <div style="float:left">
                                        <img id="captchaImage" title="캡차이미지" src="" alt="캡차이미지"/>
                                        <div id="ccaudio" style="display:none"></div>
                                    </div>
                                </div>
                                <div style="padding:3px">
                                    <input id="reload" type="button" onclick="javaScript:getImage()" value="새로고침"/>
                                    <input id="soundOn" type="button" onclick="javaScript:audio()" value="음성듣기"/>
                                </div>
                                <div style="padding:3px">
                                    <input id="answer" name="answer" type="text" value="">
                                    <input id="check" name="check" type="button" onclick="" value="확인"/>
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
