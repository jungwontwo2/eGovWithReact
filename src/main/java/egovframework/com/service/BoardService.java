package egovframework.com.service;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.domain.Board;

import java.util.List;

public interface BoardService {
    List<Board> getBoardList(ComDefaultVO vo);
    int getBoardListTotCnt(ComDefaultVO vo);
    Board getBoardDetail(Long boardId);
    void createBoard(Board board);
    void editBoard(Board board);
    void removeBoard(Long boardId);
}
