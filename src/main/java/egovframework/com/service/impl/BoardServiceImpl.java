package egovframework.com.service.impl;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.domain.Board;
import egovframework.com.mapper.BoardMapper;
import egovframework.com.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<Board> getBoardList(ComDefaultVO vo) {
        return boardMapper.selectBoardList(vo);
    }

    @Override
    public int getBoardListTotCnt(ComDefaultVO vo) {
        return boardMapper.selectBoardListTotCnt(vo);
    }

    @Override
    public Board getBoardDetail(Long boardId) {
        return boardMapper.selectBoardDetail(boardId);
    }

    @Override
    public void createBoard(Board board) {
        boardMapper.insertBoard(board);
    }

    @Override
    public void editBoard(Board board) {
        boardMapper.updateBoard(board);
    }

    @Override
    public void removeBoard(Long boardId) {
        boardMapper.deleteBoard(boardId);
    }
}
