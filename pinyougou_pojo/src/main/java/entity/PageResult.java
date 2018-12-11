package entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ricky
 * @version 1.0
 * @description 分页数据实体类
 * @date 2018/12/4
 */
public class PageResult implements Serializable {
    /**
     * 总条数
     */
    private Long total;
    /**
     * 当前页数据
     */
    private List rows;

    public PageResult(Long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
