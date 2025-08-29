package egovframework.com.service;

import egovframework.com.domain.Board;

import java.util.List;

public interface BoardService {
    List<Board> getBoardList();
    Board getBoardDetail(Long boardId);
    void createBoard(Board board);
    void editBoard(Board board);
    void removeBoard(Long boardId);
}
