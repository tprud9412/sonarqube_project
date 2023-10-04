package gu.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;

public class RequestWrapper extends HttpServletRequestWrapper {
    private static Pattern[] patterns = new Pattern[] {
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)

    };



    public RequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }



    @Override

    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];

        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }
        return encodedValues;
    }



    @Override

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }



    @Override

    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }



    private String stripXSS(String value) {

        if (value != null) {
            // null 문자를 제거한다.
            value = value.replaceAll("\0", "");

            // 패턴을 포함하는 입력에 대해 <, > 을 인코딩한다.
            for (Pattern scriptPattern : patterns) {
                if ( scriptPattern.matcher(value).find() ) {
                    System.out.println("match.....");
                    value=value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");   }
            }
        }
        return value;
    }

}