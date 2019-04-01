package ${package};

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import com.el.core.web.OpResult;
import com.el.core.domain.PagingResult;
import com.el.tw.app.common.TwOp;
import org.springframework.transaction.annotation.Transactional;
import ${poType.fullyQualifiedName};
import ${queryType.fullyQualifiedName};
import ${viewType.fullyQualifiedName};
import ${mapperType.fullyQualifiedName};

/**
* service 接口
* @author ${config.individual.name}
* @email ${config.individual.email}
* @date ${date}
*/
@Profile("tw")
@Slf4j
@Service
@RequiredArgsConstructor
public class ${fileName} implements ${fileName?substring(0,fileName?last_index_of('Impl'))} {

  private final ${mapperType.shortName} mapper;

    /**
     * 新建
     *
     * @param entity po类
     * @return result
     */
    @Transactional
    @Override
    public OpResult insert(${poType.shortName} entity) {
        mapper.insert(entity);
        return TwOp.OK;
    }

    /**
     * 修改
     *
     * @param entity po类
     * @return result
     */
    @Transactional
    @Override
    public OpResult update(${poType.shortName} entity) {
        mapper.updateByKey(entity);
        return TwOp.OK;
    }

    /**
     * 删除
     *
     * @param keys 主键
     * @return result
     */
    @Override
    public OpResult delete(List<${javaKeyType.shortName}> keys){
        mapper.delete(keys);
        return TwOp.OK;
    }

    /**
     * 逻辑删除
     *
     * @param keys 主键
     * @return result
     */
    @Override
    public OpResult logicalDelete(List<${javaKeyType.shortName}> keys){
        mapper.logicalDelete(keys);
        return TwOp.OK;
    }

    /**
     * 根据主键查询
     *
     * @param key 主键
     * @return result
     */
    public ${viewType.shortName} findByKey(${javaKeyType.shortName} key){
        return mapper.selectByKey(key);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return result
     */
    public PagingResult findByConditionPaging(${queryType.shortName} query){
        int count = mapper.selectCountByCondition(query);
        if(count > 0){
            return PagingResult.of(mapper.selectByCondition(query),count);
        }
        else {
            return PagingResult.empty();
        }
    }

}
