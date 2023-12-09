package javatt;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@Service
public class CategoryDao {

    //performing database operations
    JdbcTemplate template;

    public CategoryDao(JdbcTemplate template) {
        this.template = template;
    }
    public List<Category> display() throws ClassNotFoundException, SQLException {

        //rs : A ResultSet in Java represents the result set of a database query.
        return template.query("select * from category", (RowMapper)(rs, row) ->{
            Category ctg = new Category();
            ctg.setCategoryCode(rs.getString(1));
            ctg.setCategoryDescription(rs.getString(2));
            return ctg;
        });
    }
    public int insertData(final Category category){
        return template.update("insert into category values(?,?)", category.getCategoryCode(), category.getCategoryDescription());
    }

    public int EditData(final Category category, String cat){
        return template.update("update category set categoryCode=?, categoryDescription= ? where categoryCode=?", category.getCategoryCode(), category.getCategoryDescription(), cat);
    }

    public int deleteData(String cat){
        return template.update("delete from category where categoryCode=?", cat);
    }

    public List<Map<String, Object>> getCategory (String cat){
        return template.queryForList("SELECT * from category where categoryCode=?",cat);
    }

    public List<Map<String, Object>> getItem (String cat){
        return template.queryForList("SELECT * from items where catCode=?", cat);
    }



    public void setTemplate(JdbcTemplate template) {
    }
}
