package site.wangchuan.dag.common;


/**
 *
 */
public enum StatusCode {
    SUCCESS(100),
    EMPTY_SUCCESS(110),
    PART_SUCCESS(200),
    FAILURE(300),
    EXCEPTION(310),
    IO_TIMEOUT(320),
    PARAMETERS_ILLEGAL(330), PARAMETERS_EMPTY(331),
    FALLBACK(350),
    UNKOWN(999);

    int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
