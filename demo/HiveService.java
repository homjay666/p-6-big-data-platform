import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HiveService {

    @Autowired
    @Qualifier("hiveJdbcTemplate")
    private JdbcTemplate hiveJdbcTemplate;

    public List<Map<String, Object>> executeQuery(String sql) {
        return hiveJdbcTemplate.queryForList(sql);
    }

    public void executeUpdate(String sql) {
        hiveJdbcTemplate.execute(sql);
    }

    public List<String> showTables() {
        return hiveJdbcTemplate.queryForList("SHOW TABLES", String.class);
    }

    public List<Map<String, Object>> describeTable(String tableName) {
        return hiveJdbcTemplate.queryForList("DESCRIBE " + tableName);
    }
}