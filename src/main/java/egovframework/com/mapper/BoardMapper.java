package egovframework.com.mapper;

import egovframework.com.domain.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<Board> selectBoardList();
    Board selectBoardDetail(Long boardId);
    void insertBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(Long boardId);
}
