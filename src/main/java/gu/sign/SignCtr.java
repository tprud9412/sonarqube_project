package gu.sign;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import gu.admin.sign.SignDocSvc;
import gu.admin.sign.SignDocTypeVO;
import gu.common.SearchVO;
import gu.etc.EtcSvc;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignCtr {

    @Autowired
    private SignSvc signSvc;

    @Autowired
    private SignDocSvc signDocSvc;

    @Autowired
    private EtcSvc etcSvc;

    static final Logger LOGGER = LoggerFactory.getLogger(SignCtr.class);

    /**
     * 결제 받을 문서 리스트.
     */
    @RequestMapping(value = "/signListTobe", method = RequestMethod.GET)
    public String signListTobe_get(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();

        etcSvc.setCommonAttribute(userno, modelMap);

        //
        searchVO.setUserno(userno);
        searchVO.pageCalculate( signSvc.selectSignDocTobeCount(searchVO) ); // startRow, endRow
        List<?> listview  = signSvc.selectSignDocTobeList(searchVO);

        modelMap.addAttribute("searchVO", searchVO);
        modelMap.addAttribute("listview", listview);

        return "sign/SignDocListTobe";
    }
    @RequestMapping(value = "/signListTobe", method = RequestMethod.POST)
    public String signListTobe_post(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();

        etcSvc.setCommonAttribute(userno, modelMap);

        //
        searchVO.setUserno(userno);
        searchVO.pageCalculate( signSvc.selectSignDocTobeCount(searchVO) ); // startRow, endRow
        List<?> listview  = signSvc.selectSignDocTobeList(searchVO);

        modelMap.addAttribute("searchVO", searchVO);
        modelMap.addAttribute("listview", listview);

        return "sign/SignDocListTobe";
    }

    /**
     * 결제 할 문서 리스트.
     */
    @RequestMapping(value = "/signListTo", method = RequestMethod.GET)
    public String signListTo_get(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();

        etcSvc.setCommonAttribute(userno, modelMap);

        //
        if (searchVO.getSearchExt1()==null || "".equals(searchVO.getSearchExt1())) searchVO.setSearchExt1("sign");
        searchVO.setUserno(userno);
        searchVO.pageCalculate( signSvc.selectSignDocCount(searchVO) ); // startRow, endRow
        List<?> listview  = signSvc.selectSignDocList(searchVO);

        modelMap.addAttribute("searchVO", searchVO);
        modelMap.addAttribute("listview", listview);

        return "sign/SignDocList";
    }
    @RequestMapping(value = "/signListTo", method = RequestMethod.POST)
    public String signListTo_post(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();

        etcSvc.setCommonAttribute(userno, modelMap);

        //
        if (searchVO.getSearchExt1()==null || "".equals(searchVO.getSearchExt1())) searchVO.setSearchExt1("sign");
        searchVO.setUserno(userno);
        searchVO.pageCalculate( signSvc.selectSignDocCount(searchVO) ); // startRow, endRow
        List<?> listview  = signSvc.selectSignDocList(searchVO);

        modelMap.addAttribute("searchVO", searchVO);
        modelMap.addAttribute("listview", listview);

        return "sign/SignDocList";
    }

    /**
     * 기안하기. 
     */
    @RequestMapping(value = "/signDocTypeList", method = RequestMethod.GET)
    public String signDocTypeList(HttpServletRequest request, SearchVO searchVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();

        etcSvc.setCommonAttribute(userno, modelMap);

        List<?> listview  = signDocSvc.selectSignDocTypeList(searchVO);

        modelMap.addAttribute("listview", listview);

        return "sign/SignDocTypeList";
    }

    @RequestMapping(value = "/signDocForm", method = RequestMethod.GET)
    public String signDocForm(HttpServletRequest request, SignDocVO signDocInfo, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();

        etcSvc.setCommonAttribute(userno, modelMap);

        // 개별 작업
        List<?> signlist = null;
        if (signDocInfo.getDocno() == null) {	// 신규
            signDocInfo.setDocstatus("1");
            SignDocTypeVO docType = signDocSvc.selectSignDocTypeOne(signDocInfo.getDtno());
            signDocInfo.setDtno(docType.getDtno());
            signDocInfo.setDoccontents(docType.getDtcontents());
            signDocInfo.setUserno(userno);
            // 사번, 이름, 기안/합의/결제, 직책
            signlist = signSvc.selectSignLast(signDocInfo);
            String signPath = "";
            for (int i=0; i<signlist.size();i++){
                SignVO svo = (SignVO) signlist.get(i);
                signPath += svo.getUserno() + "," + svo.getUsernm() + "," + svo.getSstype() + "," + svo.getUserpos() + "||";
            }
            signDocInfo.setDocsignpath(signPath);

        } else {								// 수정
            signDocInfo = signSvc.selectSignDocOne(signDocInfo);

            // signDocInfo.getDocsignpath()를 가져와서 "||"을 기준으로 데이터 분리
            String[] dataEntries = signDocInfo.getDocsignpath().split("\\|\\|");

            boolean userMatch = false;

            for (String dataEntry : dataEntries) {
                String[] fields = dataEntry.split(","); // 데이터 필드 분리
                if (fields.length >= 4) {
                    String userNumber = fields[0].trim();

                    // user 번호와 userno를 비교
                    if (userNumber.equals(userno)) {
                        userMatch = true;
                        break;
                    }
                }
            }
            // user 번호가 일치하지 않는 경우 에러 페이지로 리턴
            if (!userMatch) {
                return "common/noAuth";
            }

            signlist = signSvc.selectSign(signDocInfo.getDocno());
        }
        modelMap.addAttribute("signDocInfo", signDocInfo);
        modelMap.addAttribute("signlist", signlist);

        return "sign/SignDocForm";
    }

    /**
     * 저장.
     */
    @RequestMapping(value = "/signDocSave", method = RequestMethod.POST)
    public String signDocSave(HttpServletRequest request, SignDocVO signDocInfo, ModelMap modelMap) {
        String userno = request.getSession().getAttribute("userno").toString();
        signDocInfo.setUserno(userno);

        signSvc.insertSignDoc(signDocInfo);

        return "redirect:/signListTobe";
    }

    /**
     * 읽기.
     */
    @RequestMapping(value = "/signDocRead", method = RequestMethod.GET)
    public String signDocRead(HttpServletRequest request, SignDocVO SignDocVO, ModelMap modelMap) {
        // 페이지 공통: alert
        String userno = request.getSession().getAttribute("userno").toString();

        etcSvc.setCommonAttribute(userno, modelMap);

        // 개별 작업
        SignDocVO signDocInfo = signSvc.selectSignDocOne(SignDocVO);

        // signDocInfo.getDocsignpath()를 가져와서 "||"을 기준으로 데이터 분리
        String[] dataEntries = signDocInfo.getDocsignpath().split("\\|\\|");

        boolean userMatch = false;

        for (String dataEntry : dataEntries) {
            String[] fields = dataEntry.split(","); // 데이터 필드 분리
            if (fields.length >= 4) {
                String userNumber = fields[0].trim();

                // user 번호와 userno를 비교
                if (userNumber.equals(userno)) {
                    userMatch = true;
                    break;
                }
            }
        }
        // user 번호가 일치하지 않는 경우 에러 페이지로 리턴
        if (!userMatch) {
            return "common/noAuth";
        }

        List<? >signlist = signSvc.selectSign(signDocInfo.getDocno());
        String signer = signSvc.selectCurrentSigner(SignDocVO.getDocno());

        modelMap.addAttribute("signDocInfo", signDocInfo);
        modelMap.addAttribute("signlist", signlist);
        modelMap.addAttribute("signer", signer);

        return "sign/SignDocRead";
    }

    /**
     * 삭제.
     */
    @RequestMapping(value = "/signDocDelete", method = RequestMethod.GET)
    public String signDocDelete(HttpServletRequest request, SignDocVO SignDocVO) {

        // 결재자 정보
        String userno = request.getSession().getAttribute("userno").toString();

        // 결재 문서 정보 조회
        SignDocVO signDocInfo = signSvc.selectSignDocOne(SignDocVO);

        // 사용자 번호를 비교하여 동일하지 않은 경우 리턴
        if(!signDocInfo.getUserno().equals(userno)){
            return "common/noAuth";
        }

        signSvc.deleteSignDoc(SignDocVO);
        return "redirect:/signListTobe";
    }

    /**
     * 결재.
     */
    @RequestMapping(value = "/signSave", method = RequestMethod.POST)
    public String signSave(HttpServletRequest request, SignVO signInfo, ModelMap modelMap) {

        // 결재자 정보
        String userno = request.getSession().getAttribute("userno").toString();
        etcSvc.setCommonAttribute(userno, modelMap);

        // 결재문서 정보 확인
        SignDocVO signDocVO = new SignDocVO();
        signDocVO.setDocno(signInfo.getDocno());

        // 결재 문서 정보 조회
        SignDocVO signDocInfo = signSvc.selectSignDocOne(signDocVO);

        // 문서의 현재 단계를 확인
        String currentDocStep = signDocInfo.getDocstep();

        // 사용자 번호와 단계를 비교하여 동일하지 않은 경우 리턴
        if (!isUserStepMatch(userno, currentDocStep, signDocInfo.getDocsignpath())) {
            return "common/noAuth";
        }

        signSvc.updateSign(signInfo);

        return "redirect:/signListTo";
    }

    // 사용자 번호와 단계를 비교하는 메서드
    private boolean isUserStepMatch(String userno, String currentDocStep, String docsignpath) {
        String[] dataEntries = docsignpath.split("\\|\\|");

        int docStep = 1;

        for (String dataEntry : dataEntries) {
            String[] fields = dataEntry.split(",");

            if (fields.length >= 4) {
                String userNumber = fields[0].trim();
                if (userNumber.equals(userno) && currentDocStep.equals(Integer.toString(++docStep))) {
                    return true; // 사용자 번호와 단계가 일치하는 경우
                }
            }
        }
        return false; // 사용자 번호와 단계가 일치하지 않는 경우
    }

    /**
     * 회수.
     */
    @RequestMapping(value = "/signDocCancel", method = RequestMethod.GET)
    public String signDocCancel(HttpServletRequest request, String docno) {
        // 결재자 정보
        String userno = request.getSession().getAttribute("userno").toString();

        // 결재문서 정보 확인
        SignDocVO signDocVO = new SignDocVO();
        signDocVO.setDocno(docno);

        // 결재 문서 정보 조회
        SignDocVO signDocInfo = signSvc.selectSignDocOne(signDocVO);

        // 사용자 번호를 비교하여 동일하지 않은 경우 리턴
        if(!signDocInfo.getUserno().equals(userno)){
            return "common/noAuth";
        }

        signSvc.updateSignDocCancel(docno);
        return "redirect:/signListTobe";
    }
}
