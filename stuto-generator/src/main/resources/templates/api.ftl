package ${package};

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.el.tw.app.core.util.ReturnModel;
import org.springframework.context.annotation.Profile;
import com.el.core.domain.PagingResult;
import com.el.tw.app.common.TwOp;
import org.springframework.web.bind.annotation.*;
import ${poType.fullyQualifiedName};
import ${queryType.fullyQualifiedName};
import ${viewType.fullyQualifiedName};
import ${serviceType.fullyQualifiedName};

/**
* API
* @author ${config.individual.name}
* @email ${config.individual.email}
* @date ${date}
*/
@Profile("tw")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ${fileName} {

  private final ${serviceType.shortName} service;

    /**
     * 新建
     *
     * @param entity po类
     * @return result
     */
    @PostMapping("/${tableName}")
    public ReturnModel insert(${poType.shortName} entity) {
        service.insert(entity);
        return ReturnModel.of(true, TwOp.OK, entity);
    }

    /**
     * 修改
     *
     * @param entity po类
     * @return result
     */
    @PutMapping("/${tableName}")
    public ReturnModel update(${poType.shortName} entity) {
        service.update(entity);
        return ReturnModel.of(true, TwOp.OK, entity);
    }

    /**
     * 删除
     *
     * @param keys 主键
     * @return result
     */
    @DeleteMapping("/${tableName}")
    public ReturnModel delete(List<${javaKeyType.shortName}> keys){
        service.delete(keys);
        return ReturnModel.of(true, TwOp.OK, null);
    }

    /**
     * 逻辑删除
     *
     * @param keys 主键
     * @return result
     */
    @PatchMapping("/${tableName}")
    public ReturnModel logicalDelete(List<${javaKeyType.shortName}> keys){
        service.logicalDelete(keys);
        return ReturnModel.of(true, TwOp.OK, null);
    }

    /**
     * 根据主键查询
     *
     * @param key 主键
     * @return result
     */
    @GetMapping("/${tableName}/{key}")
    public ${viewType.shortName} findByKey(@PathVariable ${javaKeyType.shortName} key){
        return service.findByKey(key);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return result
     */
    @GetMapping("/${tableName}")
    public PagingResult findByConditionPaging(${queryType.shortName} query){
        return service.findByConditionPaging(query);
    }

}
