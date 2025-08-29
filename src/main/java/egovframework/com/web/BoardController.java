package egovframework.com.web;


import egovframework.com.domain.Board;
import egovframework.com.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public List<Board> getBoardList() {
        return boardService.getBoardList();
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
