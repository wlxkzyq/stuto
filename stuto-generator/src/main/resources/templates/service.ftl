package ${package};

import java.util.List;
import com.el.core.web.OpResult;
import com.el.core.domain.PagingResult;
import ${poType.fullyQualifiedName};
import ${queryType.fullyQualifiedName};
import ${viewType.fullyQualifiedName};

/**
* service 接口
* @author ${config.individual.name}
* @email ${config.individual.email}
* @date ${date}
*/
public interface ${fileName} {

    /**
    * 新建
    *
    * @param entity po类
    * @return result
    */
    OpResult insert(${poType.shortName} entity);

    /**
    * 修改
    *
    * @param entity po类
    * @return result
    */
    OpResult update(${poType.shortName} entity);

    /**
    * 删除
    *
    * @param keys 主键
    * @return result
    */
    OpResult delete(List<${javaKeyType.shortName}> keys);

    /**
    * 逻辑删除
    *
    * @param keys 主键
    * @return result
    */
    OpResult logicalDelete(List<${javaKeyType.shortName}> keys);

    /**
    * 根据主键查询
    *
    * @param key 主键
    * @return result
    */
    ${viewType.shortName} findByKey(${javaKeyType.shortName} key);

    /**
    * 分页查询
    *
    * @param query 查询条件
    * @return result
    */
    PagingResult findByConditionPaging(${queryType.shortName} query);

}
