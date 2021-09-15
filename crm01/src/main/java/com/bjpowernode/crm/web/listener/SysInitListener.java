package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.Impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener  implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.out.println("服务器缓存处理数据字典开始");
        ServletContext application = servletContextEvent.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = ds.getALL();
        Set<String> set =  map.keySet();
        for (String key : set){
            application.setAttribute(key,map.get(key));
        }



    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
