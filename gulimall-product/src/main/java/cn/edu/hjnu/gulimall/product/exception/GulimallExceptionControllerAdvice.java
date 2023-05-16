package cn.edu.hjnu.gulimall.product.exception;

import cn.edu.hjnu.common.utils.BizCodeEnmu;
import cn.edu.hjnu.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "cn.edu.hjnu.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    //处理数据校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }));
        return R.error(BizCodeEnmu.VALID_EXCEPTION.getCode(),BizCodeEnmu.VALID_EXCEPTION.getMsg()).put("data",errorMap);
    }

    //处理全局异常
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        return R.error(BizCodeEnmu.UNKNOW_EXCEPTION.getCode(), BizCodeEnmu.UNKNOW_EXCEPTION.getMsg());
    }

}
