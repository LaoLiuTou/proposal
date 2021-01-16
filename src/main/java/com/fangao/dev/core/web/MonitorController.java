package com.fangao.dev.core.web;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.bean.MonitorBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping({"/crab"})
public class MonitorController extends ApiController {
    @GetMapping({"/cpu-memory"})
    public R<Map<String, Object>> x() {
        return this.success(MonitorBean.cpuMemory());
    }

    @GetMapping({"/jvm-heap-disk"})
    public R<Map<String, Object>> y() {
        return this.success(MonitorBean.jvmHeapDisk());
    }
}
