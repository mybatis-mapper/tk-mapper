package tk.mybatis.mapper;

import io.mybatis.provider.EntityTable;
import io.mybatis.provider.EntityTableFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.io.IOException;
import java.util.Properties;

public class TKEntityTableFactory implements EntityTableFactory {
  public static final Log    log          = LogFactory.getLog(TKEntityTableFactory.class);
  public static final String CONFIG       = "mapper.properties";
  public static final String CONFIG_PROP  = "mapper-config";
  public static final String TKTABLE_PROP = "tk-table";

  private final Config config;

  public TKEntityTableFactory() {
    this.config = new Config();
    this.init();
  }

  private void init() {
    Properties properties = null;
    try {
      properties = Resources.getResourceAsProperties(CONFIG);
    } catch (IOException e) {
      log.warn("通用 Mapper 配置文件 " + CONFIG + " 不存在，使用默认配置");
    }
    if (properties != null) {
      this.config.setProperties(properties);
    }
  }

  @Override
  public EntityTable createEntityTable(Class<?> entityClass, Chain chain) {
    //如果其他的方式创建了 table，这里直接返回，不当做 tk 的实体类处理
    EntityTable entityTable = chain.createEntityTable(entityClass);
    if(entityTable != null) {
      return entityTable;
    }
    //tk实体类处理
    EntityHelper.initEntityNameMap(entityClass, config);
    tk.mybatis.mapper.entity.EntityTable tkTable = EntityHelper.getEntityTable(entityClass);
    boolean autoResultMap = tkTable.getEntityClassColumns().stream().filter(column -> column.getTypeHandler() != null).findAny().isPresent();
    return EntityTable.of(entityClass).table(tkTable.getName()).autoResultMap(autoResultMap)
      .prop(CONFIG_PROP, config).prop(TKTABLE_PROP, tkTable);
  }

}
