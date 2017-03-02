package com.lims.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caiwenhong on 2017/2/28.
 */
public class Contract extends Model<Contract> {
    public static Contract contractDao = new Contract();

    public Map getItems() {
        Map temp = new HashMap();
        List<Contractitem> contractitemList = Contractitem.contractitemdao.find("SELECT * FROM `db_contract_item` WHERE contract_id=" + this.get("id"));
        List<Map> maps = new ArrayList<>();
        for (Contractitem item : contractitemList) {
            Map result = new HashMap();
            for (String t : item._getAttrNames()) {
                if (t.equals("element")) {
                    result.put("element", Element.elementDao.findById(item.get(t)));
                    continue;
                }
                if (t.equals("frequency")) {
                    result.put("frequency", Frequency.frequencyDao.findById(item.get(t)).toJsonSingle());
                    continue;
                }
                result.put(t, item.get(t));
            }
            List<ItemProject> projectList = ItemProject.itemprojectDao.find("SELECT * FROM `db_item_project` WHERE item_id=" + item.get("id"));
            List<Map> mapList = new ArrayList<>();
            for (ItemProject project : projectList) {
                Map t = new HashMap();
                t.put("id", project.get("id"));
                t.put("project", MonitorProject.monitorProjectdao.findById(project.get("project_id")));
                t.put("item_id", project.get("item_id"));
                mapList.add(t);
            }
            result.put("project", mapList);
            maps.add(result);
        }
        temp.put("items", maps);
        return temp;
    }
}