package com.github.trang.autoconfigure.mybatis;

import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.SQLUtils.FormatOption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 格式化执行的 SQL
 *
 * @author trang
 */
@Intercepts(
    {
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
    }
)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SqlFormatterInterceptor implements Interceptor {

    private final String dbType;
    private FormatOption formatOption;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        // 由于逻辑关系，只会进入一次
        if (args.length == 4) {
            // 4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            // 6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }
        // 设置格式化后的SQL
        Field field = BoundSql.class.getDeclaredField("sql");
        field.setAccessible(true);
        // 使用 Druid 提供的格式化工具
        String formattedSql = SQLUtils.format(boundSql.getSql(), dbType, formatOption);
        // FIXME druid 格式化后会存在多余空格的问题，进一步处理掉，等 druid 修复后去掉
        // FIXME 1.1.8 版本提的，预计 1.1.9 修复，已经两个版本了还没修复……
        String finalSql = formattedSql.replace(" ,", ",");
        field.set(boundSql, finalSql);
        // 注：下面的方法可以根据自己的逻辑调用多次，在分页插件中，count 和 page 各调用了一次
        return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}