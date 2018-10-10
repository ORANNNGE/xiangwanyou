package com.jeeplus.test.base;

import org.apache.shiro.SecurityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.apache.shiro.mgt.SecurityManager;
@SuppressWarnings(value = { "all" })
@WebAppConfiguration
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:*/spring-test.xml","classpath*:*/mybatis-config.xml"})
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)  
public abstract class BaseTestCase implements ApplicationContextAware{
	/**
	 * 应用的上下文
	 */
	public ApplicationContext applicationContext ;
	
	/**
	 * 在测试之前执行 加入securityManager 否則會報錯
	 */
    @Before
    public void init() {
    	SecurityManager securityManager =   
                (SecurityManager)applicationContext.getBean(SecurityManager.class);  
            SecurityUtils.setSecurityManager(securityManager); 
    }
    
    /**
     * 在测试之后执行
     */
    @After
    public void after() {
        
    }
    
    /**
     * 設置應用的上下文
     */
    @Override   
    public void setApplicationContext(ApplicationContext arg0)   
            throws BeansException {   
    			applicationContext=arg0;   
    }  
    
}
