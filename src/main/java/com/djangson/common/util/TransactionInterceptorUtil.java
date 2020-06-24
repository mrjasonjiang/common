package com.djangson.common.util;

import com.djangson.common.constant.Constants;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionInterceptorUtil {

    /**
     * 获取事务管理器拦截器对象
     * @param transactionManager
     * @return
     */
    public static TransactionInterceptor txAdvice(TransactionManager transactionManager) {

        // 只读事务，不做更新操作
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

        // 当前存在事务就使用当前事务，当前不存在事务就创建一个新事务
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setRollbackRules(List.of(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setTimeout(Constants.TX_METHOD_TIMEOUT);

        Map<String, TransactionAttribute> txMap = new HashMap<>(20);
        txMap.put("save*", requiredTx);
        txMap.put("add*", requiredTx);
        txMap.put("create*", requiredTx);
        txMap.put("do*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("remove*", requiredTx);
        txMap.put("reject*", requiredTx);
        txMap.put("cancel*", requiredTx);
        txMap.put("release*", requiredTx);

        txMap.put("get*", readOnlyTx);
        txMap.put("list*", readOnlyTx);
        txMap.put("page*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("search*", readOnlyTx);
        txMap.put("load*", readOnlyTx);
        txMap.put("*", readOnlyTx);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.setNameMap(txMap);

        return new TransactionInterceptor(transactionManager, source);
    }
}
