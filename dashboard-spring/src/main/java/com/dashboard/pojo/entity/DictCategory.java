package com.dashboard.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_category")
public class DictCategory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String deptName;
    private String categoryName;
    private String description;
}
