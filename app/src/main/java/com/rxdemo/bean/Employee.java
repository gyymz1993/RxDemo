package com.rxdemo.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
public class Employee {
    /*员工*/
    public String name;

    /*任务*/
    public List<Mission> mMissionList;

    public Employee(String name, List<Mission> missionList) {
        this.name = name;
        mMissionList = missionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Mission> getMissionList() {
        return mMissionList;
    }

    public void setMissionList(List<Mission> missionList) {
        mMissionList = missionList;
    }




    }
