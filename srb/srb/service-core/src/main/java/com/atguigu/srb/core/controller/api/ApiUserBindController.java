package com.atguigu.srb.core.controller.api;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.atguigu.srb.core.service.UserBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@RestController
@RequestMapping("/api/core/userBind")
public class ApiUserBindController {

    @Autowired
    UserBindService userBindService;

    @PostMapping("commitBind")
    public R commitBind(@RequestBody UserBindVO userBindVO, HttpServletRequest request) {
        //通过网关获得token, 将token解析后获得里面的userId,再将userId放入到header中
        String userId = request.getHeader("userId");
        //调用汇付宝api接口生成汇付宝表单
        String form = userBindService.commitBind(userBindVO, userId);
        return R.ok().data("form", form);
    }

    @PostMapping("notify")
    public String notifyBind(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> resultMap = RequestHelper.switchMap(parameterMap);
        //汇付宝绑定回调接口
        userBindService.notifyBind(resultMap);
        return "success";
    }


}

