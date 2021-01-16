package ${package.Controller};


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;

import io.swagger.annotations.Api;


/**
* <p>
* ${table.comment!} 前端控制器
* </p>
*
* @author ${author}
* @since ${date}
*/
@Api(tags = {"${package.ModuleName!}", "${table.comment!}"})
@RestController
<#assign controllerMapping="${controllerMappingHyphen}"/>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/${controllerMapping}")
public class ${table.controllerName} extends ${superControllerClass}<${table.serviceName}, ${entity}> {

    @GetMapping("/page")
    public R<IPage<${entity}>> page(${entity} ${entity?uncap_first}) {
        return success(baseService.page(getPage(), new QueryWrapper<>(${entity?uncap_first})));
    }
}
