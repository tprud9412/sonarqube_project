package gu.member;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gu.common.SearchVO;

@Service
public class MemberSvc {

    @Autowired
    private SqlSessionTemplate sqlSession;    
        
    public Integer selectSearchMemberCount(SearchVO param) {
        return sqlSession.selectOne("selectSearchMemberCount", param);
    }
    
    public List<?> selectSearchMemberList(SearchVO param) {
        return sqlSession.selectList("selectSearchMemberList", param);
    }

    //passwordCheck
    public UserVO selectMember4PasswordCheck(UserVO param) {
        return sqlSession.selectOne("selectMember4PasswordCheck", param);
    }
    
    public UserVO selectMember4Login(LoginVO param) {
        return sqlSession.selectOne("selectMember4Login", param);
    }

    public UserVO selectMember4ID(LoginVO param) {
        return sqlSession.selectOne("selectMember4ID", param);
    }

    public void updateLoginFailCnt(UserVO param, int failCnt) {
        param.setFailcnt(failCnt);
        sqlSession.update("updateLoginFailCnt", param);
    }

    
    public void insertLogIn(String param) {
        sqlSession.insert("insertLogIn", param);
    }

    public void insertLogOut(String param) {
        sqlSession.insert("insertLogOut", param);
    }
    
}
