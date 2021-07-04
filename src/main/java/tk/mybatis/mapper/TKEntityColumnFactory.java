package tk.mybatis.mapper;

import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.EntityColumnFactory;
import io.mybatis.provider.EntityField;
import io.mybatis.provider.EntityTable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static tk.mybatis.mapper.TKEntityTableFactory.TKTABLE_PROP;

public class TKEntityColumnFactory implements EntityColumnFactory {

  @Override
  public Optional<List<EntityColumn>> createEntityColumn(EntityTable entityTable, EntityField field, Chain chain) {
    tk.mybatis.mapper.entity.EntityTable tkTable = entityTable.getProp(TKTABLE_PROP);
    if (tkTable == null) {
      return chain.createEntityColumn(entityTable, field);
    }
    Optional<tk.mybatis.mapper.entity.EntityColumn> tkColumnOptional = tkTable.getEntityClassColumns().stream().filter(c -> c.getEntityField().getName().equals(field.getName())).findAny();
    if (tkColumnOptional.isPresent()) {
      tk.mybatis.mapper.entity.EntityColumn column = tkColumnOptional.get();
      EntityColumn entityColumn = EntityColumn.of(field).column(column.getColumn())
        .id(column.isId()).orderBy(column.getOrderBy())
        .insertable(column.isInsertable())
        .updatable(column.isUpdatable())
        .jdbcType(column.getJdbcType())
        .typeHandler(column.getTypeHandler());
      return Optional.of(Arrays.asList(entityColumn));
    } else {
      return chain.createEntityColumn(entityTable, field);
    }
  }

}
