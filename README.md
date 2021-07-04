# tk-mapper 兼容实现

使用新版 mybatis-mapper 将 tk-mapper 中的接口重新配置实现，
新的实现不需要任何配置，但是为了和 tk-mapper 兼容，增加了 **mapper.properties** 配置文件，
通过该文件对 tk-mapper 进行原有的各种配置。

## 支持范围

当前项目把 tk-mapper 的单元测试拷过来了，通过单元测试来测试对 tk-mapper 的支持范围。

目前常用的基本方法都可以支持，只有部分特殊方法无法支持。

### 支持的注解

TODO

### 支持的方法

TODO

### 不支持的注解

TODO

### 不支持的方法

TODO

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