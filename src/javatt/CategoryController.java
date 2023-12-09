package javatt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SessionAttributes({"id","errorMessage"})
@RequestMapping
@Controller
public class CategoryController {

    @Autowired
    CategoryDao categoryDao;

    @GetMapping(path="/category")
    public String showCategoryPage(ModelMap model) throws ClassNotFoundException, SQLException{
        List<Category> list = categoryDao.display();
        if (!list.isEmpty()){
            model.addAttribute("categoryList", list);
            model.put("id", list.get(0).getCategoryCode());
            model.put("description", list.get(0).getCategoryDescription());
        }
        return "category";
    }
    //default page
    @GetMapping(path="/")
    public String showDefaultPage(ModelMap model) throws ClassNotFoundException, SQLException{
        List<Category> list = categoryDao.display();
        if (!list.isEmpty()){
            model.addAttribute("categoryList", list);
            model.put("id", list.get(0).getCategoryCode());
            model.put("description", list.get(0).getCategoryDescription());
        }
        return "category";
    }

    //nav to add page
    @GetMapping(path="/add-todo")
    public String showtodopage(){
        return "caster";
    }
    //add function
    @PostMapping(path="/add-todo")
    public String addTodo(ModelMap model, @RequestParam String categoryCode, @RequestParam String categoryDescription) throws SQLException, ClassNotFoundException{
        List<Map<String, Object>> mapList = categoryDao.getCategory(categoryCode);

        mapList.forEach(rowMap ->{
            String idStr = (String) rowMap.get("categoryCode");
            model.put("id", idStr);
            String descStr = (String) rowMap.get("categoryDescription");
            model.put("desc", descStr);
        });

        if(!mapList.isEmpty()){
            model.put("errorMessage", "Record Existing");
            return "redirect:/category";
        }

        Category cc = new Category();
        cc.setCategoryCode(categoryCode);
        cc.setCategoryDescription(categoryDescription);
        categoryDao.insertData(cc);
        model.addAttribute("cc",cc);
        return "redirect:/category";

    }

    //nav to edit page
    @GetMapping(path="/update-todo")
    public String showUpdateToDoPage(ModelMap model, @RequestParam(defaultValue = "") String id) throws SQLException, ClassNotFoundException{

        model.put("id",id);
        List<Map<String, Object>> mapList = categoryDao.getCategory(id);

        mapList.forEach(rowMap ->{
            String idStr = (String) rowMap.get("categoryCode");
            model.put("id", idStr);
            String descStr = (String) rowMap.get("categoryDescription");
            model.put("description", descStr);
        });
        return "edit";
    }
    //edit update
    @PostMapping(path="/update-todo")
    public String showUpdate(ModelMap model, @RequestParam String categoryCode, @RequestParam String categoryDescription) throws SQLException, ClassNotFoundException{
        String idStr = (String) model.get("id");
        Category cc = new Category();

        cc.setCategoryCode(categoryCode);
        cc.setCategoryDescription(categoryDescription);

        categoryDao.EditData(cc, idStr);
        return "redirect:/";
    }
    //delete
    @GetMapping(path="/delete-todo")
    public String deleteTodo(ModelMap model, @RequestParam String id) throws SQLException, ClassNotFoundException{
        categoryDao.deleteData(id);
        model.clear();
        return "redirect:/";
    }

    //items page
    @GetMapping(path="/see-todo")
    public String seetodo(ModelMap model, @RequestParam(defaultValue = "") String id) throws SQLException, ClassNotFoundException{
        List<Map<String, Object>> mapList = categoryDao.getItem(id);

        if(mapList.isEmpty()){

            model.put("errorMessage", "No items in this category");
            return "redirect:/category";
        }
        model.addAttribute("itemlist", mapList);
        return "items";
    }

    @PostMapping(path="/see-todo")
    public String seetodo2(ModelMap model) throws SQLException, ClassNotFoundException{
        return "redirect:/";
    }
}



