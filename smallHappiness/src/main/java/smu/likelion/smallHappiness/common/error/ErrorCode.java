package smu.likelion.smallHappiness.common.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생하였습니다."),
    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    USER_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    //jwt
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 엑세스 토큰입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "권한이 없는 회원의 접근입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED, "로그아웃한 회원입니다."),
    //store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."),
    NOT_MATCHED_CERTIFICATION_NUM(HttpStatus.BAD_REQUEST, "가게 인증 번호가 일치하지 않습니다."),
    ALREADY_VISITED_STORE(HttpStatus.BAD_REQUEST, "이미 방문한 가게입니다."),
    NOT_VISITED_STORE(HttpStatus.BAD_REQUEST, "아직 방문하지 않은 가게입니다."),
    //coupon
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    //menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    //regNum
    INVALID_REGNUM(HttpStatus.BAD_REQUEST, "사업자 등록번호를 확인해주세요."),
    REGNUM_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 사업자등록번호입니다."),
    //review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다.")
    ;


    private final HttpStatus status;
    private final String message;
}
