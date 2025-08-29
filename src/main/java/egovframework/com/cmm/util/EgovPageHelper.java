package egovframework.com.cmm.util;

import egovframework.com.cmm.ComDefaultVO;

public class EgovPageHelper {

    public static EgovPageVO getPageVO(ComDefaultVO vo){
        EgovPageVO pageVO = new EgovPageVO();

        pageVO.setPageIndex(vo.getPageIndex());
        pageVO.setPageUnit(vo.getPageUnit());
        pageVO.setPageSize(vo.getPageSize());

        vo.setFirstIndex((pageVO.getPageIndex() - 1) * pageVO.getPageUnit());
        vo.setRecordCountPerPage(pageVO.getPageUnit());

        return pageVO;
    }
}
