package com.atguigu.srb.core.controller.api;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@RestController
@RequestMapping("/api/core/dict")
public class ApiDictController {
    @Autowired
    DictService dictService;

    @GetMapping("getDictListByDictCode/{dictCode}")
    public R getDictListById(@PathVariable("dictCode") String dictCode) {
        List<Dict> list = dictService.getDictListByDictCode(dictCode);
        return R.ok().data("list", list);
    }

}

