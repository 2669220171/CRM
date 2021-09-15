package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {


    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    //
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    //
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);



    public ClueServiceImpl() {
        super();
    }


    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;

        //(1)通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);


        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        //如果cus为null，说明以前没有这个客户，需要新建一个
        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getFullname());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(createTime);
            customer.getCreateBy(createBy);
            customer.setCreateTime(clue.getCreateTime());
            customer.setContactSummary(clue.getContactSummary());

            //添加客户
            int count = customerDao.save(customer);
            if (count != 1){
                flag = false;
            }
        }

        //(3)通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());

        int count1 = contactsDao.save(contacts);
        if (count1 != 1){
            flag = false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList){
            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());

            int count2 = customerRemarkDao.save(customerRemark);
            if (count2 != 1){
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());

            int count3 = contactsRemarkDao.save(contactsRemark);
            if (count3 != 1){
                flag = false;
            }

        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation: clueActivityRelationList){

            String activityId = clueActivityRelation.getClueId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());;
            contactsActivityRelation.setActivityId(activityId);;
            contactsActivityRelation.setContactsId(contacts.getId());

            int count4 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count4!=1){
                flag = false;
            }



        }

        //(6)如果有创建交易需求，创建一条交易
        if (t!=null){

            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactsId(contacts.getId());
            t.setContactSummary(clue.getContactSummary());


            int count5 = tranDao.save(t);
            if (count5!=1){
                flag = false;
            }
            //(7)如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setTranId(t.getId());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());

            int count6 = tranHistoryDao.save(tranHistory);
            if (count6 != 1){
                flag = false;
            }
        }

        //(8)删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {

            int count7 = clueRemarkDao.delete(clueRemark);
            if (count7 != 1){
                flag = false;
            }
        }

        //(9) 删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation: clueActivityRelationList){

            int count8 = clueActivityRelationDao.delete(clueActivityRelation);
            if (count8 != 1){
                flag = false;
            }
        }

        //(10) 删除线索
        int count9 = clueDao.delete(clueId);
        if (count9 != 1){
            flag = false;
        }


        return flag;
    }

    public boolean bund(String cid, String[] aids) {
        boolean flag = true;

        for (String aid:aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bund(car);
            if (count !=1){
                flag = false;
            }
        }

        return flag;
    }

    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public List<ClueRemark> getRemarkListByClueId(String clueId) {
        List<ClueRemark> crList = clueRemarkDao.getRemarkListByClueId(clueId);
        return crList;
    }

    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    public boolean update(Clue clue) {
        boolean flag = true;
        int count = clueDao.update(clue);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
        Clue clue = clueDao.getById(id);
        List<User> uList = userDao.getUserList();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("c",clue);
        map.put("uList",uList);

        return map;
    }

    public Boolean delete(String[] ids) {

        boolean flag = true;
        int count = clueDao.deleteByList(ids);
        if (count != ids.length){
            flag = false;
        }
        return flag;
    }

    public PaginationVO pageList(Map<String, Object> map) {
        //取得total
        int total = clueDao.getTotalByCondition(map);

        //取得dataList
        List<Clue> dataList = clueDao.getClueListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Clue> vo = new PaginationVO<Clue>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    public boolean save(Clue clue) {

        Boolean flag = true;

        int count = clueDao.save(clue);
        if (count != 1){
            flag = false;
        }

        return flag;

    }
}
