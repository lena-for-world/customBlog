package projectBlog.customBlog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Pagination {

    private int pageSize = 5; // 페이지 당 보여지는 게시글 최대 수
    private int blockSize = 5; // 페이징 된 버튼의 블럭 당 최대 개수
    private int page = 1; // 현재 페이지
    private int block = 1; // 현재 블럭
    private int totalListCnt; // 총 게시글 수
    private int totalPageCnt; // 총 페이지 수
    private int totalBlockCnt; // 총 블럭 수 -- blockSize로 인해 생긴 블럭의 수
    private int startPage = 1; // 블럭 시작 페이지
    private int endPage = 1; // 블럭 마지막 페이지
    private int startIndex = 0; // db 접근 시작 index
    private int prevBlock; // 이전 블럭의 마지막 페이지
    private int nextBlock; // 다음 블럭의 시작 페이지

    public Pagination(int totalListCnt, int page) {
        setPage(page);
        setTotalListCnt(totalListCnt);
        setTotalPageCnt((int) Math.ceil(totalListCnt * 1.0 / pageSize));
        setTotalBlockCnt((int) Math.ceil(totalPageCnt * 1.0 / blockSize));
        setBlock((int) Math.ceil((page * 1.0)/blockSize)); // 현재 블럭을 구한다
        setStartPage((block-1) * blockSize + 1); // 블럭 시작 페이지
        setEndPage(startPage + blockSize - 1);
        if(endPage > totalPageCnt) {
            this.endPage = totalPageCnt;
        }
        setPrevBlock((block * blockSize) - blockSize);
        if(prevBlock < 1) {
            this.prevBlock = 1;
        }
        setNextBlock((block * blockSize) + 1);
        if(nextBlock > totalPageCnt) {
            nextBlock = totalPageCnt;
        }
        setStartIndex((page-1) * pageSize); // db 접근 시작 index
    }

}