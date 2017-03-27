package course.labs.classroomquery.API;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/1/27.
 */
public class ApiException extends RuntimeException  {
    protected static final List<Integer> AUTH_ERROR_CODES = new ArrayList<>();

    static {
        AUTH_ERROR_CODES.add(21);
        AUTH_ERROR_CODES.add(22);
        AUTH_ERROR_CODES.add(23);
    }

    private APIReaponse mResult;

    public ApiException(APIReaponse Result) {
        this.mResult = Result;
    }

    public int getCode() {
        return mResult.getError_code();
    }

    @Override
    public String getMessage() {
        return mResult.getMessage();
    }

    public boolean isAuthError() {
        return AUTH_ERROR_CODES.contains(getCode());
    }

}
