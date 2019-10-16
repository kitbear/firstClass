package firstClass.body.utils;

import firstClass.body.enumeration.IUniqueIndex;
import firstClass.body.exception.ValidationError;
import firstClass.body.exception.ValidationException;
import liquibase.util.StringUtils;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wkit
 * @Date: 2019-10-15 20:49
 */
abstract public class ValidationExceptionUtil {
    /**
     * 处理DuplicationKeyException
     *
     * @param throwable     逻辑中生产的DuplicationKeyException
     * @param uks           实体表中的唯一索引列表
     * @return              处理完返回ValidationException或RuntimeException
     */
    public static RuntimeException handleUniqueIndex(DuplicateKeyException throwable, IUniqueIndex[] uks) {
        String errorMessage = throwable.getMessage();
        if (StringUtils.isEmpty(errorMessage)) {
            throw new RuntimeException(throwable);
        }
        List<ValidationError> errors = new ArrayList<>();
        for (IUniqueIndex uk : uks){
            if (errorMessage.toLowerCase().contains(uk.getIndexName())){
                errors.add(new ValidationError(uk.getPropertyName(),"duplicated.key"));
            }
        }
        if (errors.size() > 0) {
            return new ValidationException(errors);
        }
        return new RuntimeException(throwable);
    }
}
