package egovframework.com.web;


import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.util.EgovPageHelper;
import egovframework.com.cmm.util.EgovPageVO;
import egovframework.com.domain.Board;
import egovframework.com.service.BoardService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public Map<String,Object> getBoardList(@ModelAttribute ComDefaultVO vo) {

        PaginationInfo paginationInfo = new PaginationInfo();

        paginationInfo.setCurrentPageNo(vo.getPageIndex());//현재 페이지 번호
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());//한 페이지에 보일 데이터 개수
        paginationInfo.setPageSize(vo.getPageSize());//페이지 목록에 보일 페이지 개수

        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());


        int totalCount = boardService.getBoardListTotCnt(vo);
        paginationInfo.setTotalRecordCount(totalCount);

        List<Board> boardList = boardService.getBoardList(vo);

        HashMap<String, Object> result = new HashMap<>();
        result.put("list",boardList);
        result.put("pagination",paginationInfo);

        return result;
    }

    @GetMapping("/{boardId}")
    public Board getBoardDetail(@PathVariable Long boardId){
        return boardService.getBoardDetail(boardId);
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(@RequestBody Board board){
        boardService.createBoard(board);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long boardId, @RequestBody Board board){
        board.setBoardId(boardId);
        boardService.editBoard(board);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId){
        boardService.removeBoard(boardId);
        return ResponseEntity.ok().build();
    }
}
