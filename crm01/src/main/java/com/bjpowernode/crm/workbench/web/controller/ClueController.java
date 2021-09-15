package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.Impl.UserServiceImpl;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){

            //xxx(request,response);
            getUserList(request,response);

        }else if("/workbench/clue/save.do".equals(path)){

            //xxx(request,response);
            save(request,response);


        }else if ("/workbench/clue/pageList.do".equals(path)){

            pageList(request,response);

        }else if ("/workbench/clue/delete.do".equals(path)){

            delete(request,response);

        }else if ("/workbench/clue/getUserListAndClue.do".equals(path)){

            getUserListAndClue(request,response);

        }else if ("/workbench/clue/update.do".equals(path)){

            update(request,response);

        }else if ("/workbench/clue/detail.do".equals(path)){

            detail(request,response);

        }
        else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){

            getActivityListByClueId(request,response);

        }else if ("/workbench/clue/getRemarkListByByClueId.do".equals(path)){

            getRemarkListByClueId(request,response);

        }else if ("/workbench/clue/unbund.do".equals(path)){

            unbund(request,response);

        }else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){

            getActivityListByNameAndNotByClueId(request,response);

        }else if ("/workbench/clue/bund.do".equals(path)){

            bund(request,response);

        }else if ("/workbench/clue/getActivityListByName.do".equals(path)){

            getActivityListByName(request,response);

        }else if ("/workbench/clue/convert.do".equals(path)){

            convert(request,response);

        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行线索转化操作");

        String clueId = request.getParameter("clueId");

        String flag = request.getParameter("flag");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = null;

        if ("flag".equals(flag)){


            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expecctedDate = request.getParameter("expectedData");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            String createBy1 = ((User)request.getSession().getAttribute("user")).getName();

            t = new Tran();
            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expecctedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy1);
            t.setCreateTime(createTime);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flagOne = cs.convert(clueId,t,createBy);

        if (flagOne){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查询）");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList  = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response,aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行关联市场活动的操作");


        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(cid,aids);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查询+排除已经指定关联线索的列表）");

        String clueId = request.getParameter("clueId");
        String aname = request.getParameter("aname");

        Map<String ,String > map = new HashMap<String, String>();
        map.put("clueId",clueId);
        map.put("aname",aname);

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList  = as.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response,aList);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索的id,取消线索与市场活动的关系");
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response,flag);


    }

    private void getRemarkListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索的id,取得备注信息列表");

        String clueId = request.getParameter("clueId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> crList = cs.getRemarkListByClueId(clueId);

        PrintJson.printJsonObj(response,crList);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索的id,取得关联的市场活动信息列表");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList  = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页操作");
        String id = request.getParameter("id");


        ClueService sc = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = sc.detail(id);
        request.setAttribute("c",clue);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的修改操作");

        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setEditBy(editBy);
        clue.setEditTime(editTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService sc = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = sc.update(clue);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询线索信息列表和根据线索活动id查询单条记录的操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Map<String,Object> map = cs.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索的删除操作");

        String[] ids = request.getParameterValues("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response,flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询线索信息列表的操作（结合条件查询寻）");

        //名称，公司，公司座机，来源线索，所有者，手机，线索状态
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        //页数
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //行数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //越过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("pageSize",pageSizeStr);
        map.put("skipCount",skipCount);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO vo = cs.pageList(map);

        PrintJson.printJsonObj(response,vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索的添加操作");

       String id = UUIDUtil.getUUID();
       String fullname = request.getParameter("fullname");
       String appellation = request.getParameter("appellation");
       String owner = request.getParameter("owner");
       String company = request.getParameter("company");
       String job = request.getParameter("job");
       String email = request.getParameter("email");
       String phone = request.getParameter("phone");
       String website = request.getParameter("website");
       String mphone = request.getParameter("mphone");
       String state = request.getParameter("state");
       String source = request.getParameter("source");
       String createBy = ((User)request.getSession().getAttribute("user")).getName();
       String createTime = DateTimeUtil.getSysTime();
       String description = request.getParameter("description");
       String contactSummary = request.getParameter("contactSummary");
       String nextContactTime = request.getParameter("nextContactTime");
       String address = request.getParameter("address");


        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);;
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(clue);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }


}
