package egovframework.com.cmm.util;

import egovframework.com.cmm.ComDefaultVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EgovPageVO extends ComDefaultVO {
    private int totalPageCount;
    private int totalCount;
    private int firstPage;
    private int lastPage;
}
