package tk.mybatis.mapper.common.example;

import io.mybatis.provider.EntityTable;
import io.mybatis.provider.SqlScript;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.stream.Collectors;

public class TKExampleProvider {
  /**
   * TODO 重写所有方法，去掉特殊的字段，针对 tk 的 example 实现
   */
  /**
   * example 结构的动态 SQL 查询条件，用于接口参数只有一个 Example 对象时
   */
  public static final String EXAMPLE_WHERE_CLAUSE = "<where>\n" +
    " <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
    "  <foreach collection=\"oredCriteria\" item=\"criteria\">\n" +
    "    <if test=\"criteria.valid\">\n" +
    "      ${@tk.mybatis.mapper.util.OGNL@andOr(criteria)}" +
    "      <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
    "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
    "          <choose>\n" +
    "            <when test=\"criterion.noValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
    "            </when>\n" +
    "            <when test=\"criterion.singleValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value}\n" +
    "            </when>\n" +
    "            <when test=\"criterion.betweenValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
    "            </when>\n" +
    "            <when test=\"criterion.listValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
    "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
    "                #{listItem}\n" +
    "              </foreach>\n" +
    "            </when>\n" +
    "          </choose>\n" +
    "        </foreach>\n" +
    "      </trim>\n" +
    "    </if>\n" +
    "  </foreach>\n" +
    " </trim>\n" +
    " ${@tk.mybatis.mapper.util.OGNL@andNotLogicDelete(_parameter)}" +
    "</where>";

  /**
   * example 结构的动态 SQL 查询条件，用于多个参数时，Example 对应 @Param("example")
   */
  public static final String UPDATE_BY_EXAMPLE_WHERE_CLAUSE = "<where>\n" +
    " <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
    "  <foreach collection=\"example.oredCriteria\" item=\"criteria\">\n" +
    "    <if test=\"criteria.valid\">\n" +
    "      ${@tk.mybatis.mapper.util.OGNL@andOr(criteria)}" +
    "      <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
    "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
    "          <choose>\n" +
    "            <when test=\"criterion.noValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
    "            </when>\n" +
    "            <when test=\"criterion.singleValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value}\n" +
    "            </when>\n" +
    "            <when test=\"criterion.betweenValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
    "            </when>\n" +
    "            <when test=\"criterion.listValue\">\n" +
    "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
    "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
    "                #{listItem}\n" +
    "              </foreach>\n" +
    "            </when>\n" +
    "          </choose>\n" +
    "        </foreach>\n" +
    "      </trim>\n" +
    "    </if>\n" +
    "  </foreach>\n" +
    " </trim>\n" +
    " ${@tk.mybatis.mapper.util.OGNL@andNotLogicDelete(example)}" +
    "</where>";

  /**
   * 根据 Example 删除
   *
   * @param providerContext 上下文
   * @return cacheKey
   */
  public static String deleteByExample(ProviderContext providerContext) {
    return SqlScript.caching(providerContext, (entity, util) ->
      "DELETE FROM " + entity.table()
        + util.parameterNotNull("Example Criteria cannot be empty")
        + EXAMPLE_WHERE_CLAUSE);
  }

  /**
   * 根据 Example 条件批量更新实体信息
   *
   * @param providerContext 上下文
   * @return cacheKey
   */
  public static String updateByExample(ProviderContext providerContext) {
    return SqlScript.caching(providerContext, new SqlScript() {
      @Override
      public String getSql(EntityTable entity) {
        return "UPDATE " + entity.table()
          + set(() -> entity.updateColumns().stream().map(
          column -> column.columnEqualsProperty("entity.")).collect(Collectors.joining(",")))
          + parameterNotNull("Example Criteria cannot be empty")
          + UPDATE_BY_EXAMPLE_WHERE_CLAUSE;
      }
    });
  }

  /**
   * 根据 Example 条件批量更新实体不为空的字段
   *
   * @param providerContext 上下文
   * @return cacheKey
   */
  public static String updateByExampleSelective(ProviderContext providerContext) {
    return SqlScript.caching(providerContext, new SqlScript() {
      @Override
      public String getSql(EntityTable entity) {
        return "UPDATE " + entity.table()
          + set(() -> entity.updateColumns().stream().map(
          column -> ifTest(column.notNullTest("entity."),
            () -> column.columnEqualsProperty("entity.") + ",")
        ).collect(Collectors.joining(LF)))
          + parameterNotNull("Example Criteria cannot be empty")
          + UPDATE_BY_EXAMPLE_WHERE_CLAUSE;
      }
    });
  }

  /**
   * 根据 Example 条件批量查询，根据 Example 条件查询总数，查询结果的数量由方法定义
   *
   * @param providerContext 上下文
   * @return cacheKey
   */
  public static String selectByExample(ProviderContext providerContext) {
    return SqlScript.caching(providerContext, new SqlScript() {
      @Override
      public String getSql(EntityTable entity) {
        return "SELECT "
          + ifTest("distinct", () -> "distinct ")
          + choose(() -> whenTest("@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)", () ->
                                 foreach("_parameter.selectColumns", "selectColumn", ",",
                                    () -> "${selectColumn}"))
                       + otherwise(() -> entity.baseColumnAsPropertyList()))
          + " FROM " + entity.table()
          + ifParameterNotNull(() -> EXAMPLE_WHERE_CLAUSE)
          + ifTest("orderByClause != null", () -> " ORDER BY ${orderByClause}")
          + ifTest("orderByClause == null", () -> entity.orderByColumn().orElse(""));
      }
    });
  }

  /**
   * 根据 Example 条件查询总数
   *
   * @param providerContext 上下文
   * @return cacheKey
   */
  public static String countByExample(ProviderContext providerContext) {
    return SqlScript.caching(providerContext, new SqlScript() {
      @Override
      public String getSql(EntityTable entity) {
        return "SELECT COUNT("
          + choose(() -> whenTest("@tk.mybatis.mapper.util.OGNL@hasSelectColumns(_parameter)", () ->
                  ifTest("distinct", () -> "distinct ") +
                  foreach("_parameter.selectColumns", "selectColumn", ",",
                         () -> "${selectColumn}"))
                + otherwise(() -> "*"))
          + ") FROM "
          + entity.table()
          + ifParameterNotNull(() -> EXAMPLE_WHERE_CLAUSE);
      }
    });
  }
}
