package com.lims.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.lims.model.Element;
import com.lims.model.MonitorProject;
import com.lims.utils.ParaUtils;
import com.lims.utils.RenderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by caiwenhong on 2017/2/27.
 */
public class MonitorProjectController extends Controller {
    public void list() {
        try {
            int rowCount = getParaToInt("rowCount");
            int currentPage = getParaToInt("currentPage");
            String condition_temp = getPara("condition");
            Map condition = ParaUtils.getSplitCondition(condition_temp);
            if (rowCount == 0) {
                rowCount = ParaUtils.getRowCount();
            }
            String param = " WHERE 1=1 ";
            Object[] keys = condition.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                String key = (String) keys[i];
                Object value = condition.get(key);
                param += (" AND " + key + " like \"%" + value + "%\"");
            }
            Page<MonitorProject> monitorProjectPage = MonitorProject.monitorProjectdao.paginate(currentPage, rowCount, "SELECT *", "FROM `db_monitor_project`" + param);
            List<MonitorProject> monitorProjectList = monitorProjectPage.getList();
            Map results = toJson(monitorProjectList);
            results.put("currentPage", currentPage);
            results.put("totalPage", monitorProjectPage.getTotalPage());
            results.put("rowCount", rowCount);
            results.put("condition", condition_temp);
            renderJson(results);
        } catch (Exception e) {
            renderError(500);

        }
    }


    public Map toJson(List<MonitorProject> entityList) {
        Map<String, Object> json = new HashMap<>();
        try {
            List results = new ArrayList();
            for (MonitorProject monitorProject : entityList) {
                results.add(toJsonSingle(monitorProject));
            }
            json.put("results", results);
        } catch (Exception e) {
            renderError(500);
        }
        return json;
    }


    public Map toJsonSingle(MonitorProject monitorProject) {
        Map<String, Object> project = new HashMap<>();
        project.put("id", monitorProject.getInt("id"));
        project.put("name", monitorProject.get("name"));
        project.put("desp", monitorProject.get("desp"));
        project.put("department_id", monitorProject.get("department_id"));
        project.put("element_id", monitorProject.get("element_id"));
        return project;
    }

    public void create() {
        try {
            String name = getPara("name");
            String desp = getPara("desp");
            int department_id = getParaToInt("department_id");
            int element_id = getParaToInt("element_id");
            if (MonitorProject.monitorProjectdao.find("select * from `db_monitor_project` where name ='" + name + "' and desp= '" + desp + "' and department_id= '" + department_id + "' and element_id='" + element_id
                    + "'").size() != 0) {
                renderJson(RenderUtils.CODE_REPEAT);
            } else {
                MonitorProject monitorProject = new MonitorProject();
                boolean result = monitorProject.set("name", name).set("desp", desp).set("department_id", department_id).set("element_id", element_id).save();
                renderJson(result ? RenderUtils.CODE_SUCCESS : RenderUtils.CODE_ERROR);
            }
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void delete() {
        try {
            int id = getParaToInt("id");
            Boolean result = MonitorProject.monitorProjectdao.deleteById(id);
            renderJson(result ? RenderUtils.CODE_SUCCESS : RenderUtils.CODE_ERROR);
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void change() {
        try {
            int id = getParaToInt("id");
            String name = getPara("name");
            String desp = getPara("desp");
            int department_id = getParaToInt("department_id");
            int element_id = getParaToInt("element_id");
            MonitorProject monitorProject = MonitorProject.monitorProjectdao.findById(id);
            boolean result = MonitorProject.monitorProjectdao.set("id", id).set("name", name).set("desp", desp).set("department_id", department_id).set("element_id", element_id).update();
            renderJson(result ? RenderUtils.CODE_SUCCESS : RenderUtils.CODE_ERROR);
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void findById() {
        try {
            int id = getParaToInt("id");
            MonitorProject monitorProject = MonitorProject.monitorProjectdao.findById(id);
            if (monitorProject != null) {
                renderJson(toJsonSingle(monitorProject));
            } else {
                renderJson(RenderUtils.CODE_EMPTY);
            }
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void deleteAll() {
        try {
            Integer[] selected = getParaValuesToInt("selected[]");
            Boolean result = true;
            for (int i = 0; i < selected.length; i++) {
                int id = selected[i];
                result = result && MonitorProject.monitorProjectdao.deleteById(id);
                if (!result) break;
            }
            renderJson(result ? RenderUtils.CODE_SUCCESS : RenderUtils.CODE_ERROR);
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void total() {
        try {
            List<MonitorProject> monitorProjectList = MonitorProject.monitorProjectdao.find("select * from `db_monitor_project`");
            renderJson(toJson(monitorProjectList));
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void findByDepartment() {
        try {
            int department_id = getParaToInt("department_id");
            if (department_id != 0) {
                List<MonitorProject> monitorProjectList = MonitorProject.monitorProjectdao.find("SELECT * FROM `db_monitor_project` where  department_id = " + department_id);
                renderJson(toJson(monitorProjectList));
            } else {
                renderJson(RenderUtils.CODE_REPEAT);
            }
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void findByElement() {
        try {
            int element_id = getParaToInt("element_id");
            if (element_id != 0) {
                List<MonitorProject> monitorProjectList = MonitorProject.monitorProjectdao.find("SELECT * FROM `db_monitor_project` where element_id =" + element_id);
                renderJson(toJson(monitorProjectList));
            } else {
                renderJson(RenderUtils.CODE_REPEAT);
            }
        } catch (Exception e) {
            renderError(500);
        }
    }

    public void findElementList() {
        try {
            List<Element> elementList = Element.elementDao.find("SELECT * FROM `db_element`");
            for (Element element : elementList) {
                //List<MonitorProject> projectList = MonitorProject.monitorProjectdao.find("SELECT * FROM `db_monitor_project` WHERE ")
            }
        } catch (Exception e) {
            renderError(500);
        }
    }
}
