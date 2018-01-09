package cacheOfAnno;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public class SpringCacheTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-cache-anno.xml");
        AccountService s = (AccountService) context.getBean("accountServiceBean");
        // 第一次查询，应该走数据库
        System.out.print("first query...");
        s.getAccountByName("somebody");
        // 第二次查询，应该不查数据库，直接返回缓存的值
        System.out.print("======================\nsecond query...");
        s.getAccountByName("somebody");
    }

    @Test
    public void test02() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-cache-anno.xml");
        AccountService s = (AccountService) context.getBean("accountServiceBean");
        // 第一次查询，应该走数据库
        System.out.print("first query...");
        s.getAccountByName("somebody");
        // 第二次查询，应该不查数据库，直接返回缓存的值
        System.out.print("second query...");
        s.getAccountByName("somebody");
        System.out.println();

        System.out.println("start testing clear cache..."); // 更新某个记录的缓存，首先构造两个账号记录，然后记录到缓存中
        Account account1 = s.getAccountByName("somebody1");
        Account account2 = s.getAccountByName("somebody2");
        // 开始更新其中一个
        account1.setId(1212);
        s.updateAccount(account1);
        s.getAccountByName("somebody1");// 因为被更新了，所以会查询数据库
        s.getAccountByName("somebody2");// 没有更新过，应该走缓存
        s.getAccountByName("somebody1");// 再次查询，应该走缓存
        // 更新所有缓存
        s.reload();
        s.getAccountByName("somebody1");// 应该会查询数据库
        s.getAccountByName("somebody2");// 应该会查询数据库
        s.getAccountByName("somebody1");// 应该走缓存
        s.getAccountByName("somebody2");// 应该走缓存
    }
}

