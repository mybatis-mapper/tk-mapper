# 兼容

>下一步计划
>将 test 重新拷进来（备份）执行看看情况，看看哪些还有必要进行支持
>考虑支持 NameStyle 转换规则

## 支持的注解

- 类上必须有 `@Table` 注解，否则不识别；
- 只能在字段上使用 `@Column` 注解，并且只支持 `name` 属性，不支持 `insertable`, `updateable` 等属性；
- 只能在字段上使用 `@Id` 字段，必须有且仅有一个主键字段；

如果你目前的用法不满足上面的条件，就不能直接升级到这个版本。

## 不支持的功能

- 安全更新，如果更新条件Example空，会更新全表
- 安全删除，如果实体或Example空，会清库
- 表和字段不会自动转驼峰，必须自己指定

## 移除的方法

### Condition相关所有方法

- ConditionMapper
- DeleteByConditionMapper
- SelectByConditionMapper
- SelectCountByConditionMapper
- UpdateByConditionMapper
- UpdateByConditionSelectiveMapper

和 Example 完全一样，只是改了个名字。

### RowBounds 相关所有方法

- RowBoundsMapper
- SelectByConditionRowBoundsMapper
- SelectByExampleRowBoundsMapper
- SelectRowBoundsMapper

直接用分页插件实现分页。

### Ids 相关方法

- DeleteByIdsMapper
- SelectByIdsMapper

这两个方法可以很容易的通过 Example 实现，而且可以通过接口中的 default 方法实现通用。