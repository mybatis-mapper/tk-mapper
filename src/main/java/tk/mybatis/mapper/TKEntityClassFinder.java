package tk.mybatis.mapper;

import io.mybatis.provider.defaults.GenericEntityClassFinder;

import java.lang.reflect.Method;
import java.util.Optional;

public class TKEntityClassFinder extends GenericEntityClassFinder {

  @Override
  public Optional<Class<?>> findEntityClass(Class<?> mapperType, Method mapperMethod) {
    //通用Mapper接口第一个泛型类型就是实体类
    return super.getEntityClassByMapperMethodAndMapperType(mapperType, mapperMethod);
  }

  @Override
  public boolean isEntityClass(Class<?> clazz) {
    return true;
  }

  @Override
  public int getOrder() {
    return super.getOrder() + 200;
  }

}
